package pdm_1718i.yamda.extensions

/**
 * Created by Edgar on 31/10/2017.
 */


fun String.Companion.removePref(path: String?, count: Int): String {
    return path?.substring(count)?:""
}