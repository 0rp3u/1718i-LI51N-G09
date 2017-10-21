package pdm_1718i.yamda.data.server

/**
 * Created by orpheu on 10/13/17.
 */
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import pdm_1718i.yamda.R
import pdm_1718i.yamda.ui.App
import org.json.JSONObject
import pdm_1718i.yamda.data.MoviesDataSource
import pdm_1718i.yamda.model.Movie
import java.util.HashMap

class TMDBService : ServiceInterface, MoviesDataSource {
    val gson: Gson = Gson()
    val TAG = TMDBService::class.java.simpleName
    val basePath = App.instance.getString(R.string.TMDB_URL)
    val API_KEY = App.instance.getString(R.string.API_KEY)

    override fun post(uriBuilder: Uri.Builder, body: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        val jsonObjReq = object : JsonObjectRequest(Method.POST,
                uriBuilder
                        .scheme("https")
                        .encodedAuthority(basePath)
                        .appendQueryParameter("api_key", API_KEY)
                        .toString()
                ,
                body,
                Response.Listener<JSONObject> { response ->
                    Log.d(TAG, "/post request OK! Response: $response")
                    completionHandler(response)
                },
                Response.ErrorListener { error ->
                    VolleyLog.e(TAG, "/post request fail! Error: ${error.message}")
                    completionHandler(null)
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json")
                return headers
            }
        }

        App.instance.addToRequestQueue(jsonObjReq, TAG)
    }

    override fun get(uriBuilder: Uri.Builder, completionHandler: (response: JSONObject?) -> Unit) {
        val jsonObjReq = object : JsonObjectRequest(Method.POST,
                uriBuilder
                        .scheme("https")
                        .encodedAuthority(basePath)
                        .appendQueryParameter("api_key", API_KEY)
                        .toString()
                , null,
                Response.Listener<JSONObject> { response ->
                    Log.d(TAG, "/get request OK! Response: $response")
                    completionHandler(response)
                },
                Response.ErrorListener { error ->
                    VolleyLog.e(TAG, "/get request fail! Error: ${error.message}")
                    completionHandler(null)
                }){}

        App.instance.addToRequestQueue(jsonObjReq, TAG)
    }


    override fun movieSearch(query: String, page : Int, completionHandler: (movies: List<Movie>) -> Unit) {

        get(
                Uri.Builder()
                        .appendEncodedPath("search/movie")
                        .appendQueryParameter("query", query),
                {
                    completionHandler(if (it  != null) DataMapper().mapToMovieList((it)) else listOf())
                }
        )
    }
}