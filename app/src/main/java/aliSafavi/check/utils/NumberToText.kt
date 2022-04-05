package aliSafavi.check.utils

class NumberToText constructor(val number: String) {

    private val onesNum = mapOf(
        '0' to "",
        '1' to "یک",
        '2' to "دو",
        '3' to "سه",
        '4' to "چهار",
        '5' to "پنج",
        '6' to "شش",
        '7' to "هفت",
        '8' to "هشت",
        '9' to "نه"
    )

    private val tensNumB = mapOf(
        "10" to "ده",
        "11" to "یازده",
        "12" to "دوازده",
        "13" to "سیزده",
        "14" to "چهارده",
        "15" to "پانزده",
        "16" to "شانزده",
        "17" to "هفده",
        "18" to "هشده",
        "19" to "نانزده"
    )

    private val tensNum = mapOf(
        '1' to "ده",
        '2' to "بیست",
        '3' to "سی",
        '4' to "چهل",
        '5' to "پنجاه",
        '6' to "شصت",
        '7' to "هفتاد",
        '8' to "هشتاد",
        '9' to "نود"
    )

    private val hundredsNum = mapOf(
        '1' to "صد",
        '2' to "دویست",
        '3' to "سیصد",
        '4' to "چهارصد",
        '5' to "پانصد",
        '6' to "ششصد",
        '7' to "هفتصد",
        '8' to "هشتصد",
        '9' to "نهصد"
    )

    private val units =
        arrayOf("", "هزار", "میلیون", "میلیارد", "تریلیون", "کادریلون", "کـَـنتیلیون", "سیکستیلیون")
    var outIfRangeMsg = "بیش از حد مجاز"

    fun getTriple(num: String): String {
        var result = ""
        var r = 1
        result += when (num.length) {
            1 -> onesNum.get(num[0])
            2 -> {
                if (num[0] == '1') {
                    tensNumB.get(num[0].toString() + num[1].toString())
                } else if (num[0] == '0') ""
                else tensNum.get(num[0])
            }
            3 -> hundredsNum.get(num[0])
            else -> ""
        }
        if (num.length == 2 && num[0] == '1')
            r = 2
        return if (num.length == 0) ""
        else if (num[0] == '0')
            return getTriple(num.removeRange(0, r))
        else (result + " و " + getTriple(num.removeRange(0, r))).removeSuffix(" و ")
    }

    override fun toString(): String {
        var result = ""
        var input = number

        val len = input.length
        if (len>24)
            return outIfRangeMsg

        var level = if (len % 3 == 0) len / 3 - 1
        else len / 3
        while (level >= 0) {
            (input.length % 3).also {
                if (it == 0) {
                    result += getTriple(input.substring(0, 3))
                    input = input.removeRange(0, 3)
                } else {
                    result += getTriple(input.substring(0, it))
                    input = input.removeRange(0, it)
                }
                if (!result.endsWith(" و "))
                    result += " " + units[level] + " و "
                level--
            }
        }
        return result.removeSuffix(" و ")
    }
}