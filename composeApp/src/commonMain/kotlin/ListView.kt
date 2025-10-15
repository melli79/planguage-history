/*
 * Copyright (c) 2024.  Use under Apache Free License 2.0.
 */

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun ListView(state :LangState, selectLanguage :(PLanguage?)->Unit, selectAuthor :(Author?)->Unit) {
    val currentLanguage = state.selectedLanguage
    if (currentLanguage != null) {
        DetailView(currentLanguage, selectLanguage, selectAuthor)
    } else if (state.selectedAuthor != null) {
        AuthorListView(state.selectedAuthor, selectLanguage, Modifier)
    } else HistoryListView(state, selectLanguage)
}

@OptIn(ExperimentalSerializationApi::class)
@Composable
private fun HistoryListView(state :LangState, selectLanguage :(PLanguage?)->Unit) {
    val scrollState = rememberSaveable(saver= LazyListState.Saver) {
        val pos = state.history.indexOfFirst { it.name == "Kotlin" }
        LazyListState(if (pos>0) pos-1  else 0)
    }
    val coroutineScope = rememberCoroutineScope()

    LanguageManager.postInit {
        coroutineScope.launch {
            val pos = state.history.indexOfFirst { it.name == "Kotlin" }
            if (pos>=0)
                scrollState.scrollToItem(pos)
        }
    }
    LazyColumn(
        Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,
        state = scrollState,
    ) {
        items(state.history) { lang ->
            LanguageListItemView(lang, selectLanguage, Modifier)
        }
    }
}

@Composable
private fun AuthorListView(author :Author, setSelectedLanguage :(PLanguage?)->Unit, modifier :Modifier) {
    val scrollState = rememberSaveable(saver= LazyListState.Saver) {
        LazyListState(0)
    }
    Column(modifier) {
        Text("${author.name} (${author.affiliation})", modifier = Modifier.padding(5.dp), fontSize = 32.sp)
        LazyColumn(
            Modifier.fillMaxWidth().weight(1f), horizontalAlignment = Alignment.CenterHorizontally,
            state = scrollState,
        ) {
            items(author.languages.sortedBy { it.inception }) { lang ->
                LanguageListItemView(lang, setSelectedLanguage, Modifier)
            }
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Composable
private fun DetailView(currentLanguage :PLanguage, setSelectedLanguage :(PLanguage?)->Unit, setSelectedAuthor :(Author?)->Unit) {
    Column {
        Text(currentLanguage.toString(),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue
        )
        Text("Predecessors:")
        if (currentLanguage.fullParents.isEmpty())
            Text(" --",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(25.dp)
            )
        LazyColumn(Modifier.padding(15.dp).fillMaxWidth().weight(1f)) {
            items(currentLanguage.fullParents.sortedBy { it.inception }) { p ->
                LanguageListItemView(p, setSelectedLanguage, Modifier)
            }
        }
        Text("Creators:")
        if (currentLanguage.authors.isEmpty())
            Text(" --",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(25.dp)
            )
        LazyColumn(Modifier.padding(15.dp).fillMaxWidth().weight(1f)) {
            items(currentLanguage.authors.sortedBy { it.author.name }) { cr ->
                AuthorListItemView(cr, setSelectedAuthor, Modifier)
            }
        }
        val children = currentLanguage.fullChildren
            .sortedBy { it.inception }
        Text("Descendants:")
        if (children.isEmpty())
            Text(" --",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(25.dp)
            )
        LazyColumn(Modifier.padding(15.dp).fillMaxWidth().weight(1f)) {
            items(children) { ch ->
                LanguageListItemView(ch, setSelectedLanguage, Modifier)
            }
        }
    }
}
