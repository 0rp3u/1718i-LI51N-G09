package pdm_1718i.yamda.data

import android.graphics.Bitmap
import android.widget.ImageView
import pdm_1718i.yamda.data.server.TMDBService
import pdm_1718i.yamda.model.DetailedMovie
import pdm_1718i.yamda.model.Movie

class MoviesProvider() {

    companion object {

        val SOURCE by lazy { TMDBService() }
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

    fun searchMovies(query: String, page : Int): List<Movie>{
        return SOURCE.movieSearch(query, page)
    }

    fun movieDetail(id: Int): DetailedMovie{

        return SOURCE.movieDetail(id)
    }

    fun image(image_id: String, imageView: ImageView, imageOption: String){
        return SOURCE.movieImage(image_id, imageView , imageOption)
    }

    fun image(image_id: String, bitmapCompletionHandler: (bitmap: Bitmap)-> Unit, imageOption: String){
        SOURCE.movieImage(image_id, bitmapCompletionHandler , imageOption)
    }
}