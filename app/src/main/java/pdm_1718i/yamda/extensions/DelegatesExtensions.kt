package pdm_1718i.yamda.extensions

/**
 * Created by Red on 30/10/2017.
 */

fun runIf(func: () -> Boolean, body: () -> Any): Boolean{
    if(func()) {
        body()
        return true
    }else{
        return false
    }
}

fun runIf(pred: Boolean, body: () -> Any): Boolean{
    if(pred) {
        body()
        return true
    }else{
        return false
    }
}

fun Boolean.caseTrue(body: () -> Any): Boolean{
    if (this){
        body()
        return true
    }else{
        return false
    }
}