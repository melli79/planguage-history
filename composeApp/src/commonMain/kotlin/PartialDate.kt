data class PartialDate(val year :Short, val month :Byte? = null, val day :Byte? =null) :Comparable<PartialDate> {
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
