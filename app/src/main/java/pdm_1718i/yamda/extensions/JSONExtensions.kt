package pdm_1718i.yamda.extensions

import android.widget.ImageView
import org.json.JSONArray
import org.json.JSONObject
import com.android.volley.toolbox.ImageLoader.ImageContainer
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageLoader



/**
 * Created by orpheu on 10/19/17.
 */

fun JSONArray.asSequence() =
        (0 until length()).asSequence().map { get(it) as JSONObject }
