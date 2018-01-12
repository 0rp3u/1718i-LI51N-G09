package pdm_1718i.yamda.extensions

fun Int.toBoolean(): Boolean{
    return when(this){
        0 ->    false
        else -> true
    }
}