/*
 * Copyright (c) 2024.  Use under Apache Free License 2.0.
 */

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun LanguageListItemView(lang :PLanguage, setSelectedLanguage :(PLanguage?)->Unit, modifier :Modifier =Modifier) {
    TextButton(modifier= modifier, onClick = { setSelectedLanguage(lang) }) {
        Text(lang.toString(), fontSize = 20.sp, color = Color.Black)
    }
}

@Composable
fun AuthorListItemView(author :AuthorAff, setSelectedAuthor :(Author?)->Unit, modifier :Modifier =Modifier) {
    TextButton(modifier= modifier, onClick = { setSelectedAuthor(author.author) }) {
        Text(author.toString(), fontSize = 20.sp, color = Color.Black)
    }
}
