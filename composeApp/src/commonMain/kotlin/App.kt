import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val history = listOf(
            PLanguage("Basic", PartialDate(1964), creators = setOf("J.G. Kemeny", "T.E. Kurtz")),
            PLanguage("Basic", PartialDate(1974), version = "4", creators = setOf("MAI BASIC Four Inc.")),
            PLanguage("Basic", PartialDate(1975), version = "Altair", creators = setOf("B. Gates", "P. Allen")),
            PLanguage("Basic", PartialDate(1976), version = "Integer", creators = setOf("St. Wozniak")),
            PLanguage("Basic", PartialDate(1977), version = "Commodore", creators = setOf("J. Tramiel")),
            PLanguage("Basic", PartialDate(1977), version = "Applesoft", creators = setOf("M. McDonald", "R. Weiland")),
            PLanguage("Basic", PartialDate(1980), version = "Applesoft III", creators = setOf("Apple Inc.")),
            PLanguage("Basic", PartialDate(1980), version = "Applesoft III MS", creators = setOf("Microsoft Inc.")),
            PLanguage("javaScript", PartialDate(1995, 12), creators = setOf("B. Eich")),
            PLanguage("javaScript", PartialDate(1997), version = "ECMA", creators = setOf("EXMA TC39-TG1")),
            PLanguage("Json", PartialDate(2000), creators = setOf("D. Crockford @PayPal")),
            PLanguage("javaScript", PartialDate(2009,12), version = "ES5", creators = setOf("ECMA Task Force")),
            PLanguage("Rust", PartialDate(2010), version = "0.1", creators = setOf("G. Hoare")),
            PLanguage("Kotlin", PartialDate(2011), version = "0.1", creators = setOf("JetBrains Inc.")),
            PLanguage("TypeScript", PartialDate(2012,10), version = "0.8", creators = setOf("A. Heijlsberg")),
            PLanguage("TypeScript", PartialDate(2013), version = "0.9", creators = setOf("A. Heijlsberg")),
            PLanguage("Swift", PartialDate(2014), creators = setOf("Apple Inc.")),
            PLanguage("TypeScript", PartialDate(2014), version = "1.0", creators = setOf("A. Heijlsberg")),
            PLanguage("Rust", PartialDate(2015,5,15), version = "1.0", creators = setOf("G. Hoare")),
            PLanguage("Kotlin", PartialDate(2016,2,15), version = "1.0", creators = setOf("JetBrains Inc.")),
            PLanguage("TypeScript", PartialDate(2016,9,22), version = "2.0", creators = setOf("A. Hejlsberg")),
            PLanguage("Kotlin", PartialDate(2017,11,28), version = "1.2", creators = setOf("JetBrains Inc.")),
            PLanguage("TypeScript", PartialDate(2018,7,30), version = "3.0", creators = setOf("A. Hejlsberg")),
            PLanguage("Kotlin", PartialDate(2018,10,29), version = "1.3", creators = setOf("JetBrains Inc.")),
            PLanguage("TypeScript", PartialDate(2020,8,20), version = "4.0", creators = setOf("A. Hejlsberg")),
            PLanguage("Rust", PartialDate(2021,2,11), version = "1.50", creators = setOf("Rust Foundation", "Mozilla Foundation", "Google Inc", "Microsoft Inc", "AWS", "Huawei")),
            PLanguage("Rust", PartialDate(2022,4,7), version = "1.60", creators = setOf("Rust Foundation")),
            PLanguage("Kotlin", PartialDate(2022,7), version = "1.7", creators = setOf("JetBrains Inc.")),
            PLanguage("TypeScript", PartialDate(2022,11,15), version = "4.0", creators = setOf("A. Hejlsberg")),
            PLanguage("Kotlin", PartialDate(2022,12), version = "1.8", creators = setOf("JetBrains Inc.")),
            PLanguage("TypeScript", PartialDate(2023,3,16), version = "5.0", creators = setOf("A. Hejlsberg")),
            PLanguage("Rust", PartialDate(2023,6,1), version = "1.70", creators = setOf("Rust Foundation")),
            PLanguage("Kotlin", PartialDate(2023,7,6), version = "1.9", creators = setOf("A. Breslav @JetBrains Inc.")),
            PLanguage("Kotlin", PartialDate(2024,5,21), version = "2.0", creators = setOf("A. Breslav @JetBrains Inc.")),
            PLanguage("Rust", PartialDate(2024,7,25), version = "1.80", creators = setOf("Rust Foundation")),
        )
        LazyColumn(Modifier.padding(5.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items (history) { lang ->
                Text(
                    text = "${lang.name} ${lang.version} (*${lang.inception})",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(2.dp)
                )
            }
        }
    }
}
