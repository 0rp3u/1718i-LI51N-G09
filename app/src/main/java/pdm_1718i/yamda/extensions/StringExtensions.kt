package pdm_1718i.yamda.extensions

/**
 * Created by Edgar on 31/10/2017.
 */


fun String?.removePref(count: Int): String {
    return this?.substring(count)?:""
}
