import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(PartialDateDeSerializer::class)
data class PartialDate(val year :Short, val month :Byte? = null, val day :Byte? =null) :Comparable<PartialDate> {

    companion object {
        fun fromString(input :String) :PartialDate {
            if (input.isBlank())
                throw IllegalArgumentException("Invalid partial Date")
            val parts = input.split("-", "/", ".")
            val year = parts[0].toShort()
            val month = if (parts.size>=2) parts[1].toByte() else null
            val day = if (parts.size>=3) parts[2].toByte() else null
            return PartialDate(year, month, day)
        }
    }

    override fun compareTo(other :PartialDate) :Int {
        if (year!=other.year)  return year - other.year
        if (month!=null && other.month!=null)
            if (month!=other.month) return month - other.month
            else if (day!=null && other.day!=null)
                return day - other.day
        return 0
    }

    override fun toString() :String {
        if (month==null)  return year.toString()
        if (day==null)  return "$year-$month"
        return "$year-$month-$day"
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(PartialDate::class)
class PartialDateDeSerializer : KSerializer<PartialDate> {
    override fun serialize(encoder :Encoder, value :PartialDate) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder :Decoder) = PartialDate.fromString(decoder.decodeString())
}
