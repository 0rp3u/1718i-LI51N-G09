package pdm_1718i.yamda.data

import android.widget.ImageView
import pdm_1718i.yamda.data.cache.DomainCache
import pdm_1718i.yamda.data.server.TMDBService
import pdm_1718i.yamda.model.DetailedMovie
import pdm_1718i.yamda.model.Movie



class MoviesProvider {

    companion object {
        val SOURCE by lazy { DomainCache(TMDBService())}
    }

    fun nowPlayingMovies (page:Int, completionHandler:(movies:List<Movie>) -> Unit){
        SOURCE.playingMovies(page, completionHandler)
    }

    fun upcomingMovies (page:Int, completionHandler:(movies:List<Movie>) -> Unit){
        SOURCE.upcomingMovies(page, completionHandler)
    }

    fun popularMovies (page:Int, completionHandler:(movies:List<Movie>) -> Unit) {
        SOURCE.popularMovies(page, completionHandler)
    }

    fun searchMovies(query: String, page : Int, completionHandler: (movies: List<Movie>) -> Unit) {
        SOURCE.movieSearch(query, page, completionHandler)
    }

    fun movieDetail(id: Int, completionHandler: (movie : DetailedMovie) -> Unit){
        SOURCE.movieDetail(id, completionHandler)
    }

    fun image(image_id: String, imageView: ImageView, imageOption: String){
        SOURCE.movieImage(image_id.substring(1), imageView , imageOption)
    }
}