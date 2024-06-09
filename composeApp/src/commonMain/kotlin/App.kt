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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import programminglanguages.composeapp.generated.resources.Res

@OptIn(ExperimentalSerializationApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        var history by remember { mutableStateOf(HistoryManager.getAllPLangs()) }
        HistoryManager.updater = { newLanguages -> history = newLanguages }
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

@OptIn(ExperimentalResourceApi::class)
@ExperimentalSerializationApi
object HistoryManager :PLanguage.LanguageProvider {

    private var the = History()
    var updater :((List<PLanguage>)->Unit)? =null

    init {
        GlobalScope.launch {
            val src :ByteArray = Res.readBytes("files/planguages.json")
            the = deSerializer.decodeFromString(src.decodeToString())
            normalize()
            updater?.invoke(getAllPLangs())
        }
    }

    @Serializable
    class History  {

        @Serializable
        data class Header(
            val title :String = "History of Programming Languages",
            val author :Author =Author(name = "M. Gr.", email = "melchiorG@gMail.com"),
            val date :PartialDate = PartialDate(2024,6,9),
            val copyright :String = "FDL"
        ) {}

        val header = Header()

        val langs = mutableSetOf(
            PLanguage("Basic", inception= PartialDate(1964), authors = setOf(Author("J.G. Kemeny"), Author("T.E. Kurtz"))),
            PLanguage("Basic", version = "4", PartialDate(1974), authors = setOf(Author("MAI BASIC Four Inc."))),
            PLanguage("Basic", version = "Altair", PartialDate(1975), authors = setOf(Author("B. Gates", "P. Allen"))),
            PLanguage("Basic", version = "Integer", PartialDate(1976), authors = setOf(Author("St. Wozniak"))),
            PLanguage("Basic", version = "Commodore", PartialDate(1977), authors = setOf(Author("J. Tramiel"))),
            PLanguage("Basic", version = "Applesoft", PartialDate(1977), authors = setOf(Author("M. McDonald"), Author("R. Weiland"))),
            PLanguage("Basic", version = "Applesoft III", PartialDate(1980), authors = setOf(Author("Apple Inc."))),
            PLanguage("Basic", version = "Applesoft III MS", PartialDate(1980), authors = setOf(Author("Microsoft Inc."))),
            PLanguage("javaScript", inception= PartialDate(1995, 12), authors = setOf(Author("B. Eich"))),
            PLanguage("javaScript", version = "ECMA", PartialDate(1997), authors = setOf(Author("EXMA TC39-TG1"))),
            PLanguage("Json", inception= PartialDate(2000), authors = setOf(Author("D. Crockford", affiliation = "PayPal"))),
            PLanguage("javaScript", version = "ES5", PartialDate(2009,12), authors = setOf(Author("ECMA Task Force"))),
            PLanguage("Rust", version = "0.1", PartialDate(2010), authors = setOf(Author("G. Hoare"))),
            PLanguage("Kotlin", version = "0.1", PartialDate(2011), authors = setOf(Author("JetBrains Inc."))),
            PLanguage("TypeScript", version = "0.8", PartialDate(2012,10), authors = setOf(Author("A. Heijlsberg", affiliation = "Microsoft inc."))),
            PLanguage("TypeScript", version = "0.9", PartialDate(2013), authors = setOf(Author("A. Heijlsberg", affiliation = "Microsoft inc."))),
            PLanguage("Swift", inception= PartialDate(2014), authors = setOf(Author("Apple Inc."))),
            PLanguage("TypeScript", version = "1.0", PartialDate(2014), authors = setOf(Author("A. Heijlsberg", affiliation = "Microsoft inc."))),
            PLanguage("Rust", version = "1.0", PartialDate(2015,5,15), authors = setOf(Author("G. Hoare"))),
            PLanguage("Kotlin", version = "1.0", PartialDate(2016,2,15), authors = setOf(Author("JetBrains Inc."))),
            PLanguage("TypeScript", version = "2.0", PartialDate(2016,9,22), authors = setOf(Author("A. Heijlsberg", affiliation = "Microsoft inc."))),
            PLanguage("Kotlin", version = "1.2", PartialDate(2017,11,28), authors = setOf(Author("JetBrains Inc."))),
            PLanguage("TypeScript", version = "3.0", PartialDate(2018,7,30), authors = setOf(Author("A. Heijlsberg", affiliation = "Microsoft inc."))),
            PLanguage("Kotlin", version = "1.3", PartialDate(2018,10,29), authors = setOf(Author("JetBrains Inc."))),
            PLanguage("TypeScript", version = "4.0", PartialDate(2020,8,20), authors = setOf(Author("A. Heijlsberg", affiliation = "Microsoft inc."))),
            PLanguage("Rust", version = "1.50", PartialDate(2021,2,11), authors = setOf(Author("Rust Foundation"), Author("Mozilla Foundation"), Author("Google Inc"), Author("Microsoft Inc"), Author("AWS", "Huawei"))),
            PLanguage("Rust", version = "1.60", PartialDate(2022,4,7), authors = setOf(Author("Rust Foundation"))),
            PLanguage("Kotlin", version = "1.7", PartialDate(2022,7), authors = setOf(Author("JetBrains Inc."))),
            PLanguage("TypeScript", version = "4.0", PartialDate(2022,11,15), authors = setOf(Author("A. Heijlsberg", affiliation = "Microsoft inc."))),
            PLanguage("Kotlin", version = "1.8", PartialDate(2022,12), authors = setOf(Author("JetBrains Inc."))),
            PLanguage("TypeScript", version = "5.0", PartialDate(2023,3,16), authors = setOf(Author("A. Heijlsberg", affiliation = "Microsoft inc."))),
            PLanguage("Rust", version = "1.70", PartialDate(2023,6,1), authors = setOf(Author("Rust Foundation"))),
            PLanguage("Kotlin", version = "1.9", PartialDate(2023,7,6), authors = setOf(Author("A. Breslav", affiliation= "JetBrains Inc."))),
            PLanguage("Kotlin", version = "2.0", PartialDate(2024,5,21), authors = setOf(Author("A. Breslav", affiliation= "JetBrains Inc."))),
            PLanguage("Rust", version = "1.80", PartialDate(2024,7,25), authors = setOf(Author("Rust Foundation"))),
        )
    }

    private fun normalize() {
        val that = this
        if (the.langs==null || the.langs.isEmpty())
            throw RuntimeException("Failed reading any languages from 'planguages.json'")
        the.langs.forEach{lang -> lang.normalize(that)}
        print("Parsed ${the.langs.size} language versions.\n")
    }

    fun getAllPLangs() = the.langs.sortedBy { it.inception }
    override fun findByName(name :String) = the.langs.filter { l -> l.name==name }
    override fun findByNameAndYear(name :String, year :Short) = the.langs
        .filter { l -> l.name==name&&(l.inception==null||l.inception.year<year) }
        .sortedBy() { l -> l.inception?.year?.unaryMinus() ?: -1945 }

    fun findByNameLike(pattern :Regex) = the.langs.filter { l -> l.name.matches(pattern) }
    fun count() = the.langs.size

    fun computeMostInfluential() :List<Pair<PLanguage, Int>> {
        val result = mutableMapOf<PLanguage, Int>()
        for (plang in the.langs) {
            for (parent in plang.fullParents) {
                result[parent] = (result[parent] ?: 0) +1
            }
        }
        return result.entries.sortedBy { en -> -en.value }
            .take(20).map { en -> Pair(en.key, en.value) }
    }

    fun computeChildren(plang: PLanguage) = the.langs.filter { child -> plang in child.fullParents }

    fun computeSeries() = the.langs.groupBy { p -> p.name }
        .entries.sortedBy { -it.value.size }.take(15)
}
