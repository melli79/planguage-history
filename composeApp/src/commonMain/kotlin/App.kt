/*
 * Copyright (c) 2024.  Use under Apache Free License 2.0.
 */

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import org.jetbrains.compose.ui.tooling.preview.Preview

data class LangState @OptIn(ExperimentalSerializationApi::class) constructor(
    val searchTerm :String = "",
    val selectedLanguage :PLanguage? =null,
    val selectedAuthor :Author? =null,
    val history :List<PLanguage> = LanguageManager.getAllPLangs()
) {}

data class ClickHistory(val last :MutableList<PLanguage?> =mutableListOf()) {
    fun goBack() = last.removeLastOrNull()

    fun push(current :PLanguage?) {
        if (last.lastOrNull()!=current)
            last.add(current)
    }
}

@OptIn(ExperimentalSerializationApi::class, ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
@Preview
fun App(requestBack :Boolean = false) {
    val coroutineScope = rememberCoroutineScope()
    coroutineScope.launch {
        LanguageManager.initialize()
    }
    MaterialTheme {
        var state by remember { mutableStateOf(LangState()) }
        var isPortrait by remember { mutableStateOf(true) }
        var savedLanguage by rememberSaveable { mutableStateOf<String?>(null) }
        val clickHistory by remember { mutableStateOf(ClickHistory()) }
        var isBack by remember { mutableStateOf(requestBack) }
        if (isBack) {
            val lastLanguage = clickHistory.goBack()
            if (state.selectedLanguage==null && lastLanguage==null)
                state = state.copy(searchTerm= state.searchTerm.dropLast(1))
            else
                state = state.copy(selectedLanguage= lastLanguage)
            savedLanguage= null
            isBack = false
        }
        if (state.selectedLanguage == null && savedLanguage != null) {
            println("saved state: '$savedLanguage'")
            val components = savedLanguage!!.split(';')
            val selectedLanguage =
                LanguageManager.findByNameAndVersion(components.first(), components.last())
            state = LangState(selectedLanguage= selectedLanguage)
            clickHistory.push(selectedLanguage)
            savedLanguage = null
        }
        Scaffold(topBar= { TopAppBar(title= { FlowRow(modifier= Modifier.fillMaxWidth()) {
            Text("History of Programming Languages",
                modifier= Modifier.align(Alignment.CenterVertically),
                maxLines = 1, overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Text("Search:  ", Modifier.padding(2.dp).align(Alignment.CenterVertically))
            BasicTextField(state.searchTerm,
                modifier= Modifier.width(100.dp).height(30.dp).align(Alignment.CenterVertically).border(1.dp, Color.White),
                singleLine= true,
                textStyle= TextStyle(fontSize = 24.sp, color = Color.White),
                onValueChange= { input :String ->
                    val newText = input.ifBlank { "" }
                    LanguageManager.filter = newText
                    clickHistory.push(state.selectedLanguage)
                    savedLanguage = null
                    state = state.copy(searchTerm= newText, selectedLanguage= null)
                })
        }}, navigationIcon= {
            IconButton(onClick= {
                val lastLanguage = clickHistory.goBack()
                if (state.selectedLanguage==null && lastLanguage==null)
                    state = state.copy(searchTerm= state.searchTerm.dropLast(1))
                else {
                    state = state.copy(selectedLanguage= lastLanguage)
                }
                savedLanguage= "${lastLanguage?.name};${lastLanguage?.version ?: ""}"
            }) {
                Icon(imageVector= Icons.AutoMirrored.Filled.ArrowBack, "back")
            }
        })}) {
            Box(modifier= Modifier.fillMaxSize()
                .onSizeChanged { size -> isPortrait = size.width <= size.height }) {
                if (isPortrait)
                    ListView(state, { newLanguage :PLanguage? ->
                        clickHistory.push(state.selectedLanguage)
                        state = state.copy(selectedLanguage= newLanguage, selectedAuthor= null)
                        savedLanguage = "${newLanguage?.name};${newLanguage?.version ?: ""}"
                    }) { newAuthor :Author? ->
                        state = state.copy(selectedAuthor= newAuthor, selectedLanguage= null)
                    }
                else
                    MapView(state) { newLanguage :PLanguage? ->
                        clickHistory.push(state.selectedLanguage)
                        state = state.copy(selectedLanguage= newLanguage)
                        savedLanguage = "${newLanguage?.name};${newLanguage?.version ?: ""}"
                    }
            }
        }
        LanguageManager.updater = { newLanguages -> state = state.copy(history= newLanguages) }
    }
}
