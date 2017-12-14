package pdm_1718i.yamda.extensions

fun Int.toBoolean(): Boolean?{
    return when(this){
        0 ->    false
        1 ->    true
        else -> null
    }
}