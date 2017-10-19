package pdm_1718i.yamda.data.server

/**
 * Created by orpheu on 10/13/17.
 */


import android.net.Uri
import org.json.JSONObject


interface ServiceInterface {
    fun post(uriBuilder: Uri.Builder, body: JSONObject, completionHandler: (response: JSONObject?) -> Unit)
    fun get(uriBuilder: Uri.Builder, completionHandler: (response: JSONObject?) -> Unit)
}