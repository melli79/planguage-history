/*
 * Copyright (c) 2024.  Use under Apache Free License 2.0.
 */

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.ExperimentalResourceApi
import programminglanguages.composeapp.generated.resources.Res
import kotlin.math.min

@OptIn(ExperimentalResourceApi::class)
@ExperimentalSerializationApi
object LanguageManager : PLanguage.LanguageProvider {

    var filter :String = ""
        set(value) {
            field = value
            updater?.invoke(getFiltered(value))
        }

    private var the = History()
    var updater :((List<PLanguage>)->Unit)? =null
    var historyLength = 365*(124-46)+6*30+24
        private set
    private var inited = false
    private var postInit = {}

    suspend fun initialize() {
        val src = Res.readBytes("files/planguages.json").decodeToString()
        the = deSerializer.decodeFromString(src)
        normalize()
        computeChildren()
        computeAuthors()
        historyLength = the.langs.maxOf { it.inception }.toDays()
        updater?.invoke(getAllPLangs())
        inited = true
        postInit()
    }

    fun postInit(post :()->Unit) {
        this.postInit = post
        if (inited) post()
    }

    @Serializable
    class History  {

        @Serializable
        data class Header(
            val title :String = "History of Programming Languages",
            val author :Author = Author(name = "M. Gr.", email = "melchiorG@gMail.com"),
            val date :PartialDate = PartialDate(2024, 6, 9),
            val copyright :String = "FDL"
        ) {}

        val header = Header()

        val langs = mutableSetOf(
            PLanguage(
                "Basic",
                inception = PartialDate(1964),
                authors = setOf(authorOf("J.G. Kemeny"), authorOf("T.E. Kurtz"))
            ),
            PLanguage(
                "Basic",
                version = "4",
                PartialDate(1974),
                authors = setOf(authorOf("MAI BASIC Four Inc."))
            ),
            PLanguage(
                "Basic",
                version = "Altair",
                PartialDate(1975),
                authors = setOf(authorOf("B. Gates"), authorOf("P. Allen"))
            ),
            PLanguage(
                "Basic",
                version = "Integer",
                PartialDate(1976),
                authors = setOf(authorOf("St. Wozniak"))
            ),
            PLanguage(
                "Basic",
                version = "Commodore",
                PartialDate(1977),
                authors = setOf(authorOf("J. Tramiel"))
            ),
            PLanguage(
                "Basic",
                version = "Applesoft",
                PartialDate(1977),
                authors = setOf(authorOf("M. McDonald"), authorOf("R. Weiland"))
            ),
            PLanguage(
                "Basic",
                version = "Applesoft III",
                PartialDate(1980),
                authors = setOf(authorOf("Apple Inc."))
            ),
            PLanguage(
                "Basic",
                version = "Applesoft III MS",
                PartialDate(1980),
                authors = setOf(authorOf("Microsoft Inc."))
            ),
            PLanguage(
                "javaScript",
                inception = PartialDate(1995, 12),
                authors = setOf(authorOf("B. Eich"))
            ),
            PLanguage(
                "javaScript",
                version = "ECMA",
                PartialDate(1997),
                authors = setOf(authorOf("EXMA TC39-TG1"))
            ),
            PLanguage(
                "Json",
                inception = PartialDate(2000),
                authors = setOf(authorOf("D. Crockford", affiliation = "PayPal"))
            ),
            PLanguage(
                "javaScript",
                version = "ES5",
                PartialDate(2009, 12),
                authors = setOf(authorOf("ECMA Task Force"))
            ),
            PLanguage(
                "Rust",
                version = "0.1",
                PartialDate(2010),
                authors = setOf(authorOf("G. Hoare"))
            ),
            PLanguage(
                "Kotlin",
                version = "0.1",
                PartialDate(2011),
                authors = setOf(authorOf("JetBrains Inc."))
            ),
            PLanguage(
                "TypeScript",
                version = "0.8",
                PartialDate(2012, 10),
                authors = setOf(authorOf("A. Heijlsberg", affiliation = "Microsoft inc."))
            ),
            PLanguage(
                "TypeScript",
                version = "0.9",
                PartialDate(2013),
                authors = setOf(authorOf("A. Heijlsberg", affiliation = "Microsoft inc."))
            ),
            PLanguage(
                "Swift",
                inception = PartialDate(2014),
                authors = setOf(authorOf("Apple Inc."))
            ),
            PLanguage(
                "TypeScript",
                version = "1.0",
                PartialDate(2014),
                authors = setOf(authorOf("A. Heijlsberg", affiliation = "Microsoft inc."))
            ),
            PLanguage(
                "Rust",
                version = "1.0",
                PartialDate(2015, 5, 15),
                authors = setOf(authorOf("G. Hoare"))
            ),
            PLanguage(
                "Kotlin",
                version = "1.0",
                PartialDate(2016, 2, 15),
                authors = setOf(authorOf("JetBrains Inc."))
            ),
            PLanguage(
                "TypeScript",
                version = "2.0",
                PartialDate(2016, 9, 22),
                authors = setOf(authorOf("A. Heijlsberg", affiliation = "Microsoft inc."))
            ),
            PLanguage(
                "Kotlin",
                version = "1.2",
                PartialDate(2017, 11, 28),
                authors = setOf(authorOf("JetBrains Inc."))
            ),
            PLanguage(
                "TypeScript",
                version = "3.0",
                PartialDate(2018, 7, 30),
                authors = setOf(authorOf("A. Heijlsberg", affiliation = "Microsoft inc."))
            ),
            PLanguage(
                "Kotlin",
                version = "1.3",
                PartialDate(2018, 10, 29),
                authors = setOf(authorOf("JetBrains Inc."))
            ),
            PLanguage(
                "TypeScript",
                version = "4.0",
                PartialDate(2020, 8, 20),
                authors = setOf(authorOf("A. Heijlsberg", affiliation = "Microsoft inc."))
            ),
            PLanguage(
                "Rust",
                version = "1.50",
                PartialDate(2021, 2, 11),
                authors = setOf(
                    authorOf("Rust Foundation"),
                    authorOf("Mozilla Foundation"),
                    authorOf("Google Inc"),
                    authorOf("Microsoft Inc"),
                    authorOf("AWS", "Huawei")
                )
            ),
            PLanguage(
                "Rust",
                version = "1.60",
                PartialDate(2022, 4, 7),
                authors = setOf(authorOf("Rust Foundation"))
            ),
            PLanguage(
                "Kotlin",
                version = "1.7",
                PartialDate(2022, 7),
                authors = setOf(authorOf("JetBrains Inc."))
            ),
            PLanguage(
                "TypeScript",
                version = "4.0",
                PartialDate(2022, 11, 15),
                authors = setOf(authorOf("A. Heijlsberg", affiliation = "Microsoft inc."))
            ),
            PLanguage(
                "Kotlin",
                version = "1.8",
                PartialDate(2022, 12),
                authors = setOf(authorOf("JetBrains Inc."))
            ),
            PLanguage(
                "TypeScript",
                version = "5.0",
                PartialDate(2023, 3, 16),
                authors = setOf(authorOf("A. Heijlsberg", affiliation = "Microsoft inc."))
            ),
            PLanguage(
                "Rust",
                version = "1.70",
                PartialDate(2023, 6, 1),
                authors = setOf(authorOf("Rust Foundation"))
            ),
            PLanguage(
                "Kotlin",
                version = "1.9",
                PartialDate(2023, 7, 6),
                authors = setOf(authorOf("A. Breslav", affiliation = "JetBrains Inc."))
            ),
            PLanguage(
                "Kotlin",
                version = "2.0",
                PartialDate(2024, 5, 21),
                authors = setOf(authorOf("A. Breslav", affiliation = "JetBrains Inc."))
            ),
            PLanguage(
                "Rust",
                version = "1.80",
                PartialDate(2024, 7, 25),
                authors = setOf(authorOf("Rust Foundation"))
            ),
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
    fun getFiltered(prefix :String) :List<PLanguage> {
        return if (prefix.endsWith(' ')) findByName(prefix.trim()).sortedBy { it.inception }
        else findByNameLike(prefix).sortedBy { it.inception }
    }
    override fun findByName(name :String) = the.langs.filter { l -> l.name==name }
    override fun findByNameAndInception(name :String, inception :PartialDate) = the.langs
        .filter { l -> l.name==name&&l.inception<inception }
        .sortedByDescending() { l -> l.inception }

    fun findByNameLike(prefix :String) = the.langs.filter { l -> l.name.startsWith(prefix, ignoreCase= true) }
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

    private fun normalize(name :String) :String {
        if (name.isBlank()) return ""
        val segments = name.trim().split("\\s+".toRegex())
        val initial = segments[0].substring(0..<min(2, segments[0].length)).lowercase()
        val last = segments.last().lowercase()
        val name = if (last=="inc") segments[segments.size-2].lowercase()  else last
        return "$initial $name"
    }

    fun computeAuthors() {
        val name2author = mutableMapOf<String, Author>()
        for (lang in the.langs) {
            for (aa in lang.authors) {
                val name = normalize(aa.author.name)
                val candidate = name2author[name]
                if (candidate!=null) {
                    aa.author = candidate
                    candidate.affiliation = aa.affiliation
                    candidate.languages.add(lang)
                } else {
                    name2author[name] = aa.author
                    aa.author.languages.add(lang)
                }
                val aff = normalize(aa.affiliation ?: continue)
                var company = name2author[aff]
                if (company==null) {
                    company = Author(name = aa.affiliation)
                    name2author[aff] = company
                }
                company.languages.add(lang)
            }
        }
    }

    fun computeChildren() {
        for (lang in the.langs) {
            for (parent in lang.fullParents) {
                parent.fullChildren.add(lang)
            }
        }
    }

    fun computeSeries() = the.langs.groupBy { p -> p.name }
        .entries.sortedBy { -it.value.size }.take(15)

    fun findByNameAndVersion(name :String, version :String) :PLanguage? = the.langs
        .find { it.name==name && it.version==version }
}
