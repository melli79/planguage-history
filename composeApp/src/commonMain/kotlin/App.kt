/*
 * Copyright (c) 2024.  Use under Apache Free License 2.0.
 */

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.ExperimentalSerializationApi
import org.jetbrains.compose.ui.tooling.preview.Preview

data class LangState @OptIn(ExperimentalSerializationApi::class) constructor(
    val searchTerm :String = "",
    val selectedLanguage :PLanguage? =null,
    val history :List<PLanguage> = HistoryManager.getAllPLangs()
) {}

@OptIn(ExperimentalSerializationApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        var state by remember { mutableStateOf(LangState()) }
        var isPortrait by remember { mutableStateOf(true) }
        var lastLanguage by rememberSaveable { mutableStateOf<String?>(null) }
        if (state.selectedLanguage == null && lastLanguage != null) {
            println("saveed state: '$lastLanguage'")
            val components = lastLanguage!!.split(';')
            state = LangState(selectedLanguage =
                HistoryManager.findByNameAndVersion(components.first(), components.last()))
            lastLanguage = null
        }
        Column(Modifier.fillMaxSize().padding(5.dp)) {
            Row(Modifier.fillMaxWidth()) {
                Spacer(Modifier.weight(1f))
                Text("Search:  ", Modifier.padding(2.dp).align(Alignment.CenterVertically))
                BasicTextField(state.searchTerm,
                    modifier = Modifier.width(100.dp).border(1.dp, Color.Black)
                        .align(Alignment.CenterVertically),
                    textStyle = TextStyle(fontSize = 20.sp),
                    singleLine = true,
                    onValueChange = { input :String ->
                        val newText = input.ifBlank { "" }
                        HistoryManager.filter = newText
                        lastLanguage = null
                        state = state.copy(searchTerm = newText, selectedLanguage = null)
                    })
            }
            Box(modifier = Modifier.fillMaxSize()
                .onSizeChanged { size -> isPortrait = size.width <= size.height }) {
                if (isPortrait)
                    ListView(state) { newLanguage :PLanguage? ->
                        state = state.copy(selectedLanguage = newLanguage)
                        lastLanguage = "${newLanguage?.name};${newLanguage?.version}"
                    }
                else
                    MapView(state) { newLanguage :PLanguage? ->
                        state = state.copy(selectedLanguage = newLanguage)
                        lastLanguage = "${newLanguage?.name};${newLanguage?.version}"
                    }
            }
        }
        HistoryManager.updater = { newLanguages -> state = state.copy(history= newLanguages) }
    }
}
