package pdm_1718i.yamda.extensions

fun String?.removePref(count: Int): String {
    return this?.substring(count)?:""
}
