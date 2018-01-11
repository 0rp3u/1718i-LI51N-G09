package pdm_1718i.yamda.data

import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import pdm_1718i.yamda.data.db.MoviesDb
import pdm_1718i.yamda.data.server.TMDBService
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.model.MovieDetail

class MoviesProvider {

    companion object {
        val tmdbAPI  by lazy {
            TMDBService()
        }

        val moviesDatabase  by lazy {
            MoviesDb(tmdbAPI)
        }

        val SOURCE = moviesDatabase
    }

    fun nowPlayingMovies (page:Int) :List<Movie>{
        return SOURCE.playingMovies(page)
    }

    fun upcomingMovies (page:Int) :List<Movie>{
        return SOURCE.upcomingMovies(page)
    }

    fun popularMovies (page:Int) :List<Movie> {
        return SOURCE.popularMovies(page)
    }

    fun followingMovies (page: Int) :List<Movie> {
        return SOURCE.followingMovies(page)
    }

    fun searchMovies(query: String, page : Int): List<Movie>{
        return SOURCE.movieSearch(query, page)
    }

    fun movieDetail(id: Int): MovieDetail {

        return SOURCE.movieDetail(id)
    }

    fun image(image_id: String, imageView: ImageView, imageOption: String){
        return SOURCE.movieImage(image_id, imageView , imageOption)
    }

    fun image(image_id: String, imageOption: String, bitmapCompletionHandler: (bitmap: Bitmap)-> Unit){
        SOURCE.movieImage(image_id, imageOption, bitmapCompletionHandler)
    }

}