package pdm_1718i.yamda.extensions

/**
 * Created by Red on 01/11/2017.
 */

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
