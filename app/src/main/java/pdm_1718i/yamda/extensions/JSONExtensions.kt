package pdm_1718i.yamda.extensions

import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by orpheu on 10/19/17.
 */

fun JSONArray.asSequence() =
        (0 until length()).asSequence().map { get(it) as JSONObject }