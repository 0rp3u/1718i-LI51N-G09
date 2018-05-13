package pdm_1718i.yamda.extensions


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
