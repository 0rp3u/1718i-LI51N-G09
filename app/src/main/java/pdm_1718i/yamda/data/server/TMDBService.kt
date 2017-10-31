package pdm_1718i.yamda.data.server

/**
 * Created by orpheu on 10/13/17.
 */
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.pdm_1718i.yamda.data.server.MovieDetailResult
import com.example.pdm_1718i.yamda.data.server.MovieSearchResult
import com.google.gson.Gson
import org.json.JSONObject
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.MoviesDataSource
import pdm_1718i.yamda.extensions.getImageListener
import pdm_1718i.yamda.model.DetailedMovie
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App
import java.util.*

class TMDBService : ServiceInterface, MoviesDataSource {
    companion object {
        private val gson: Gson = Gson()
        val TAG = TMDBService::class.java.simpleName
        val basePath = App.instance.getString(R.string.TMDB_URL)
        val API_KEY = App.instance.getString(R.string.API_KEY)
        val IMAGE_PATH = App.instance.getString(R.string.TMDB_IMAGE_URL)
        val DEFAULT_PAGINATION: Int = 1
    }

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
                    Log.d(TAG, "/post request fail! Error: ${error.message}")
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

    override fun get(uriBuilder: Uri.Builder, completionHandler: (response: JSONObject) -> Unit) {

        val jsonObjReq = object : JsonObjectRequest(
                Method.GET,
                uriBuilder
                    .scheme("https")
                    .encodedAuthority(basePath)
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("language", Locale.getDefault().toString())
                    .toString(),
                null,
                Response.Listener<JSONObject> { response ->
                    Log.d(TAG, "/get request OK! Response: $response")
                    completionHandler(response)
                },
                Response.ErrorListener { error ->
                    Log.d(TAG, "/get request fail! Error: ${error?.message}")
                    Toast.makeText(App.instance, "Something Went KABOOM", Toast.LENGTH_SHORT).show()
                }) {
        }

        App.instance.addToRequestQueue(jsonObjReq, TAG)
    }


    override fun popularMovies(page: Int, completionHandler: (movies: List<Movie>) -> Unit) {
            get(
                    Uri.Builder()
                            .appendEncodedPath("movie/popular"),
                    {
                        completionHandler(DataMapper().mapToMovieList(gson.fromJson(it.toString(), MovieSearchResult::class.java)))

                    }
            )
    }

    override fun upcomingMovies(page: Int, completionHandler: (movies: List<Movie>) -> Unit) {
        get(
                Uri.Builder()
                    .appendEncodedPath("movie/upcoming"),
                {
                    completionHandler(DataMapper().mapToMovieList(gson.fromJson(it.toString(), MovieSearchResult::class.java)))
                }
        )
    }

    override fun playingMovies (page: Int, completionHandler: (movies: List<Movie>) -> Unit) {
        get(
                Uri.Builder()
                    .appendEncodedPath("movie/now_playing"),
                {
                    completionHandler(DataMapper().mapToMovieList(gson.fromJson(it.toString(), MovieSearchResult::class.java)))
                }
        )
    }


    override fun movieSearch(query: String, page: Int, completionHandler: (movies: List<Movie>) -> Unit) {

        get(
                Uri.Builder()
                    .appendEncodedPath("search/movie")
                    .appendQueryParameter("query", query),
                {
                    completionHandler(DataMapper().mapToMovieList(gson.fromJson(it.toString(), MovieSearchResult::class.java)))
                }
        )
    }

    override fun movieDetail(id: Int, completionHandler: (movies:DetailedMovie) -> Unit) {

        get(
                Uri.Builder()
                    .appendEncodedPath("movie/$id"),
                {
                    completionHandler(DataMapper().mapToMovieDetail(gson.fromJson(it.toString(), MovieDetailResult::class.java)))
                }
        )
    }

    override fun movieImage(image_id: String, imageView : ImageView, image_size: String) {
        val uri = Uri.Builder()
                .appendEncodedPath(image_size)
                .appendEncodedPath(image_id)
                .scheme("https")
                .encodedAuthority(IMAGE_PATH)
                .toString()


        if(App.imageLoader.isCached(uri,imageView.height, imageView.width)){
            Log.d("cache", "$uri was cached!")
        }

        imageView.tag = uri
        App.imageLoader
                .get(uri, getImageListener(imageView, R.drawable.ic_loading, R.drawable.ic_movie_thumbnail, uri))

    }
}