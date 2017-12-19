package pdm_1718i.yamda.extensions

import java.util.concurrent.ConcurrentHashMap


fun <K, R> memoize(fn: (K)->R?): (K)->R? {
    val map = ConcurrentHashMap<K, R?>()

    return {
        key -> map.getOrElse(key){
        val res = fn(key)
        if (res != null)
            map.put(key, res)
        res
    }
    }
}