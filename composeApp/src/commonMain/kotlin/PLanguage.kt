import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

@Serializable
data class PLanguage(val name :String, var version :String ="",
                     val inception :PartialDate,
                     val description :String? = null,
                     val authors :Set<AuthorAff> = setOf(),
                     val parents :MutableSet<PLanguageRef> = mutableSetOf(),
                     @Transient var fullParents :MutableSet<PLanguage> = mutableSetOf(),
                     @Transient var fullChildren :MutableSet<PLanguage> = mutableSetOf(),
) {
    interface LanguageProvider {
        fun findByName(name :String) :List<PLanguage>
        fun findByNameAndInception(name :String, inception :PartialDate) :List<PLanguage>
    }

    override fun toString() = "$name $version (*$inception)"

    private fun normalizeParents(history :LanguageProvider) {
        fullParents = parents?.mapNotNull { p ->
            val candidates = history.findByNameAndInception(p.name, inception)
            if (p.version != null) candidates.firstOrNull { l -> l.version == p.version } ?: candidates.firstOrNull()
            else candidates.firstOrNull()
        }?.toMutableSet() ?: mutableSetOf()
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
        val parents = parents?.joinToString(", ") { l -> l.name + "/" + l.version } ?: ""
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

@Serializable(with = AuthorDeSerializer::class)
data class AuthorAff(var author :Author, val affiliation :String? =null, val eMail :String? =null) {
    override fun toString() = author.name + if (affiliation!=null) "($affiliation)" else ""
}

object AuthorDeSerializer : KSerializer<AuthorAff> {
    override val descriptor :SerialDescriptor = buildClassSerialDescriptor("AuthorAff") {
        element("name", PrimitiveSerialDescriptor("name", PrimitiveKind.STRING))
        element("affiliation", PrimitiveSerialDescriptor("affiliation", PrimitiveKind.STRING), isOptional = true)
        element("email", PrimitiveSerialDescriptor("email", PrimitiveKind.STRING), isOptional = true)
    }

    override fun serialize(encoder :Encoder, value :AuthorAff) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeStringElement(descriptor, 0, value.author.name)
        if (value.affiliation!=null) composite.encodeStringElement(descriptor, 1, value.affiliation)
        if (value.eMail!=null) composite.encodeStringElement(descriptor, 2, value.eMail)
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder :Decoder) :AuthorAff = decoder.beginStructure(descriptor).run {
        lateinit var name :String
        var affiliation :String? =null
        var email :String? =null
        loop@while (true) { when (val index = decodeElementIndex(descriptor)) {
            0 -> name = decodeStringElement(descriptor, index)
            1 -> affiliation = decodeStringElement(descriptor, index)
            2 -> email = decodeStringElement(descriptor, index)
            CompositeDecoder.DECODE_DONE -> break@loop
            CompositeDecoder.UNKNOWN_NAME -> {
                println("Skipping unknown element at index: $index")
                continue@loop
            }
            else -> error("Unexpected Deserialization index: $index")
        } }
        endStructure(descriptor)
        authorOf(name, email, affiliation)
    }
}

fun authorOf(name :String, email :String? =null, affiliation :String? =null) :AuthorAff {
    println(name)
    return AuthorAff(Author(name, email, affiliation), affiliation, email)
}

@Serializable
data class Author(val name :String, var email :String? =null, var affiliation :String? =null,
    @Transient val languages :MutableList<PLanguage> =mutableListOf(),
) {

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
