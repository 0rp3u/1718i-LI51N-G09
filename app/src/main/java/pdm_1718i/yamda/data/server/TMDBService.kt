package pdm_1718i.yamda.data.server

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.example.pdm_1718i.yamda.data.server.MovieDetailResult
import com.example.pdm_1718i.yamda.data.server.MovieSearchResult
import com.google.gson.Gson
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.json.JSONObject
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.ConnectivityManager
import pdm_1718i.yamda.data.MoviesDataSource
import pdm_1718i.yamda.extensions.getDate
import pdm_1718i.yamda.extensions.getImageListener
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.model.MovieDetail
import pdm_1718i.yamda.ui.App
import java.util.*

class TMDBService : MoviesDataSource {
    companion object {
        private val gson: Gson = Gson()
        val TAG = TMDBService::class.java.simpleName
        val basePath = App.instance.getString(R.string.TMDB_URL)
        val API_KEY = App.instance.getString(R.string.API_KEY)
        val IMAGE_PATH = App.instance.getString(R.string.TMDB_IMAGE_URL)
        val DEFAULT_PAGINATION: Int = 1
    }

    //syncronous volley post
    fun post(uriBuilder: Uri.Builder, body: JSONObject): JSONObject {
        val future: RequestFuture<JSONObject> = RequestFuture.newFuture()
        val jsonObjReq = object : JsonObjectRequest(Method.POST,
                uriBuilder
                        .scheme("https")
                        .encodedAuthority(basePath)
                        .appendQueryParameter("api_key", API_KEY)
                        .toString(),
                body,
                Response.Listener<JSONObject> { response ->
                    Log.d(TAG, "/post request OK! Response for $uriBuilder")
                    future.onResponse(response)
                },
                Response.ErrorListener { error ->
                    Log.d(TAG, "/post request fail! Error: ${error.message}")
                    future.onErrorResponse(error)
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                with(emptyMap<String, String>() as HashMap) {
                    put("Content-Type", "application/json")
                    return this
                }
            }
        }
        App.instance.addToRequestQueue(jsonObjReq, TAG)
        return future.get()
    }


    //syncronous volley get/
    fun get(uriBuilder: Uri.Builder): JSONObject {
        if(!ConnectivityManager.isConnected()){
            return JSONObject()
        }
        val future: RequestFuture<JSONObject> = RequestFuture.newFuture()
        val jsonObjReq = object : JsonObjectRequest(Method.GET,
                uriBuilder
                        .scheme("https")
                        .encodedAuthority(basePath)
                        .appendQueryParameter("api_key", API_KEY)
                        .appendQueryParameter("language", Locale.getDefault().toString())
                        .appendQueryParameter("region", Locale.getDefault().country)
                        .toString(),
                null,
                Response.Listener<JSONObject> { response ->
                    //Log.d(TAG, "/get request OK! Response for $uriBuilder")
                    future.onResponse(response)
                },
                Response.ErrorListener { error ->
                    Log.d(TAG, "/get request fail! Error: ${error.message}")
                    future.onErrorResponse(error)
                }) {}
        App.instance.addToRequestQueue(jsonObjReq, TAG)
        return try {
            future.get()
        }catch (error: Throwable){
            JSONObject()
        }
    }



    override fun popularMovies(page: Int): List<Movie> {
        return get(
                Uri.Builder()
                        .appendEncodedPath("movie/popular")
                        .appendQueryParameter("page", "$page")
        ).let {
            DataMapper().mapToMovieList(gson.fromJson(it.toString(), MovieSearchResult::class.java))
        }
    }

    override fun upcomingMovies(page: Int): List<Movie> {
        return get(
                Uri.Builder()
                        .appendEncodedPath("movie/upcoming")
                        .appendQueryParameter("region", Locale.getDefault().country)
                        .appendQueryParameter("sort_by","release_date.asc")
                        .appendQueryParameter("release_date.gte", Calendar.getInstance().getDate())
                        .appendQueryParameter("page", "$page")
                        .appendQueryParameter("release_date.gte", Calendar.getInstance().getDate())
        ).let {
            DataMapper().mapToMovieList(gson.fromJson(it.toString(), MovieSearchResult::class.java))
        }
    }

    override fun playingMovies(page: Int): List<Movie> {
        return get(
                Uri.Builder()
                        .appendEncodedPath("movie/now_playing")
                        .appendQueryParameter("page", "$page")
        ).let {
            DataMapper().mapToMovieList(gson.fromJson(it.toString(), MovieSearchResult::class.java))
        }
    }

    override fun movieSearch(query: String, page: Int): List<Movie> {
        return get(
                Uri.Builder()
                        .appendEncodedPath("search/movie")
                        .appendQueryParameter("query", query)
                        .appendQueryParameter("page", "$page")
        ).let {
            DataMapper().mapToMovieList(gson.fromJson(it.toString(), MovieSearchResult::class.java))
        }
    }

    override fun movieDetail(id: Int): MovieDetail {
        return get(Uri.Builder().appendEncodedPath("movie/$id")).let {
            DataMapper().mapToMovieDetail(gson.fromJson(it.toString(), MovieDetailResult::class.java))
        }
    }

    override fun movieImage(image_id: String, imageView: ImageView, image_size: String){
        val uri = Uri.Builder()
                .appendEncodedPath(image_size)
                .appendEncodedPath(image_id)
                .scheme("https")
                .encodedAuthority(IMAGE_PATH)
                .toString()
        imageView.tag = uri
        when(image_size){
            ImageOption.SMALL -> App.imageLoader.get(uri, getImageListener(imageView, R.drawable.ic_loading, R.drawable.ic_movie_thumbnail,null,  uri))
            else -> App.imageLoader.get(uri, getImageListener(imageView, R.drawable.ic_loading,0, {App.moviesProvider.image(image_id, imageView, ImageOption.SMALL)}, uri))
        }
    }

    override fun movieImage(image_id: String, image_size: String, bitmapCompletionHandler: (bitmap: Bitmap) -> Unit) {
        val uri = Uri.Builder()
                .appendEncodedPath(image_size)
                .appendEncodedPath(image_id)
                .scheme("https")
                .encodedAuthority(IMAGE_PATH)
                .toString()

        when(image_size){
            ImageOption.SMALL -> App.imageLoader.get(uri, getImageListener(bitmapCompletionHandler))
            else -> App.imageLoader.get(uri, getImageListener(bitmapCompletionHandler, {App.moviesProvider.image(image_id, ImageOption.SMALL, bitmapCompletionHandler)}))
        }
    }

    override fun movieImageSync(image_id: String, image_size: String): Bitmap{

        val future: RequestFuture<Bitmap> = RequestFuture.newFuture()


        val uri = Uri.Builder()
                .appendEncodedPath(image_size)
                .appendEncodedPath(image_id)
                .scheme("https")
                .encodedAuthority(IMAGE_PATH)
                .toString()


        val listener = object : ImageLoader.ImageListener {
            override fun onErrorResponse(error: VolleyError) {
                future.onErrorResponse(error)
            }

            override fun onResponse(response: ImageLoader.ImageContainer, isImmediate: Boolean) {
                future.onResponse(response.bitmap)
            }
        }


        //hack
        async(UI) {
            App.imageLoader.get(uri, listener)
        }


        return future.get()
    }
}