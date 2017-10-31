package pdm_1718i.yamda.extensions

/**
 * Created by Red on 30/10/2017.
 */

fun runIf(func: () -> Boolean, body: () -> Any): Boolean =
    runIf(func(), body)

fun runIf(pred: Boolean, body: () -> Any): Boolean{
    if(pred)
        body()
    return pred
}

fun Boolean.caseTrue(body: () -> Any): Boolean{
    if (this)
        body()
    return this
}

fun Boolean.caseFalse(body: () -> Any): Boolean{
    if(!this)
        body()
    return this
}