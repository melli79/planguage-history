data class PLanguage(val name :String,
                     val inception :PartialDate, val version :String ="1.0",
    val creators :Set<String> = setOf(),
    val parents :MutableSet<PLanguage> = mutableSetOf(),
) {
    override fun toString() = "$name $version (*$inception)"
}
