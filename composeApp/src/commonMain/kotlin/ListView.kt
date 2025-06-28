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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun ListView(state :LangState, setSelectedLanguage :(PLanguage?)->Unit) {
    val currentLanguage = state.selectedLanguage
    if (currentLanguage != null) {
        DetailView(currentLanguage, setSelectedLanguage)
    } else HistoryListView(state, setSelectedLanguage)
}

@Composable
private fun HistoryListView(state :LangState, selectLanguage :(PLanguage?)->Unit) {
    val scrollState = rememberSaveable(saver= LazyListState.Saver) {
        val pos = state.history.indexOfFirst { it.name == "Kotlin" }
        LazyListState(pos-1)
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

@OptIn(ExperimentalSerializationApi::class)
@Composable
private fun DetailView(currentLanguage :PLanguage, setSelectedLanguage :(PLanguage?)->Unit) {
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
            items(currentLanguage.authors.sortedBy { it.name }) { cr ->
                Text(cr.toString(), fontSize = 20.sp, modifier = Modifier.padding(5.dp))
            }
        }
        val children = LanguageManager.computeChildren(currentLanguage)
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
