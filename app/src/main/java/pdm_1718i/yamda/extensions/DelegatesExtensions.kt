package pdm_1718i.yamda.extensions

fun runIf(func: () -> Boolean, body: () -> Any?): Boolean =
    runIf(func(), body)

fun runIf(pred: Boolean, body: () -> Any?): Boolean{
    if(pred)
        body()
    return pred
}