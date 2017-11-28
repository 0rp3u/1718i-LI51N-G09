package pdm_1718i.yamda.extensions

import org.json.JSONArray
import org.json.JSONObject


fun JSONArray.asSequence() =
        (0 until length()).asSequence().map { get(it) as JSONObject }
