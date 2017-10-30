package pdm_1718i.yamda.extensions

/**
 * Created by Red on 30/10/2017.
 */

fun runIf(func: () -> Boolean, body: () -> Any){
    if(func())
        body()
}

fun runIf(pred: Boolean, body: () -> Any){
    if(pred)
        body()
}