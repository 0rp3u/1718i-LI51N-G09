package pdm_1718i.yamda.extensions

/**
 * Created by Red on 30/10/2017.
 */

fun runIf(func: () -> Boolean, body: () -> Unit?): Boolean =
    runIf(func(), body)

fun runIf(pred: Boolean, body: () -> Unit?): Boolean{
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