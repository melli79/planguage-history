/*
 * Copyright (c) 2024.  Use under Apache Free License 2.0.
 */

import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun LanguageButton(lang :PLanguage, setSelectedLanguage :(PLanguage?)->Unit) {
    TextButton(onClick = { setSelectedLanguage(lang) }) {
        Text(lang.toString(), fontSize = 20.sp, color = Color.Black)
    }
}