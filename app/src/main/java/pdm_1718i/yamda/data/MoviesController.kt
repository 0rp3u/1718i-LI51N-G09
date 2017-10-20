package pdm_1718i.yamda.data

import android.net.Uri
import pdm_1718i.yamda.data.server.TMDBService
import org.json.JSONObject
import android.net.Uri.Builder
import android.widget.Toast
import pdm_1718i.yamda.data.server.DataMapper
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App

/**
 * Created by orpheu on 10/13/17.
 */


class MoviesController {
    companion object {
        val tmdbService by lazy { TMDBService() }
    }


    /*fun movieSearch(query: String, completionHandler: (movies: List<Movie>) -> Unit) {

    movieSearch(query,query, 1, completionHandler)
    }*/


        fun movieSearch(query: String, page : Int, completionHandler: (movies: List<Movie>) -> Unit) {

            tmdbService.get(
                    Uri.Builder()
                            .appendEncodedPath("search/movie")
                            .appendQueryParameter("query", query),
                    {
                        if(it!=null)completionHandler(DataMapper().mapToMovieList(it))
                        else  Toast.makeText(App.instance, "::ERROR::", Toast.LENGTH_SHORT).show()
                    }
            )
        }
}