import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json

@Serializable
data class PLanguage(val name :String, var version :String ="",
                     val inception :PartialDate,
                     val description :String? = null,
                     val authors :Set<Author> = setOf(),
                     val parents :MutableSet<PLanguageRef> = mutableSetOf(),
                     @Transient var fullParents :MutableSet<PLanguage> = mutableSetOf(),
) {
    interface LanguageProvider {
        fun findByName(name :String) :List<PLanguage>
        fun findByNameAndYear(name :String, year :Short) :List<PLanguage>
    }

    override fun toString() = "$name $version (*$inception)"

    private fun normalizeParents(history :LanguageProvider) {
        fullParents = parents?.map{ p ->
            val candidates = history.findByNameAndYear(p.name, inception?.year ?: 2022)
            if (p.version!=null) candidates.firstOrNull { l -> l.version==p.version } ?: candidates.firstOrNull()
            else candidates.firstOrNull()
        }?.filterNotNull()?.toMutableSet() ?: mutableSetOf()
    }

    fun addParent(parent :PLanguage) = fullParents.add(parent)

    override fun hashCode(): Int {
        if (name==null)  print("Error: language missing name!\n")
        return name.hashCode() +31*(version?.hashCode() ?: 0)
    }

    override fun equals(other :Any?) :Boolean {
        if (this===other) return true
        if (other !is PLanguage) return false
        return name==other.name && version==other.version
    }

    fun strictlyEqual(other :Any?) :Boolean {
        if (!equals(other)) return false
        return inception==(other as PLanguage).inception && authors==other.authors &&
                parents==other.parents
    }

    fun describe() :String { // deserialization may leave some fields null even though specified nonnull
        val parents = parents?.map{ l -> l.name +"/"+l.version }?.joinToString(", ") ?: ""
        return "PLanguage(name=\"$name\", version=\"$version\", inception=${if (inception!=null) inception else PartialDate(-1)}" +
                if (!authors.isNullOrEmpty()) ", authors=$authors)" else ")" +
                        if (!parents.isBlank()) "{$parents}" else ""
    }

    fun normalize(history :LanguageProvider) {
        val version :String? = this.version
        if (version==null||version.isBlank())
            this.version = ""
        normalizeParents(history)
    }
}

@Serializable
data class PLanguageRef(val name :String, val version :String? =null)

@Serializable
data class Author(val name :String, val email :String? =null, val affiliation :String? =null) {

    override fun toString() = "$name "+ if (affiliation!=null) "($affiliation)" else ""

    fun describe() = when {
        email==null&&affiliation==null -> name
        email==null -> "$name ($affiliation)"
        affiliation==null -> "$name ($email)"
        else -> "$name (eMail: $email, affiliation: $affiliation)"
    }
}

val deSerializer = Json {
    prettyPrint = true
}
