package pdm_1718i.yamda.data

import android.graphics.Bitmap
import pdm_1718i.yamda.data.server.TMDBService
import pdm_1718i.yamda.model.DetailedMovie
import pdm_1718i.yamda.model.Movie



class MoviesProvider {

    companion object {
        val SOURCE by lazy { TMDBService()}
    }

    fun nowPlayingMovies (query:String, page:Int, completionHandler:(movies:List<Movie>) -> Unit){
        SOURCE.playingSearch(query, page, completionHandler)
    }

    fun upcomingMovies (query:String, page:Int, completionHandler:(movies:List<Movie>) -> Unit){
        SOURCE.upcomingSearch(query, page, completionHandler)
    }

    fun getPopularMovies (query:String, page:Int, completionHandler:(movies:List<Movie>) -> Unit) {
        SOURCE.popularSearch(page, completionHandler)
    }

    fun searchMovies(query: String, page : Int, completionHandler: (movies: List<Movie>) -> Unit) {
        SOURCE.movieSearch(query, page, completionHandler)
    }

    fun movieDetail(id: Int, completionHandler: (movie : DetailedMovie) -> Unit){
        SOURCE.movieDetail(id, completionHandler)
    }

    fun getImage(image_id: String, completionHandler: (image: Bitmap) -> Unit, image_size: String = "w185"){
        SOURCE.movieImage(image_id.substring(1), completionHandler, image_size)
    }
}