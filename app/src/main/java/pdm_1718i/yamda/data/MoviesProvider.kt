package pdm_1718i.yamda.data

import pdm_1718i.yamda.data.server.TMDBService
import pdm_1718i.yamda.model.DetailedMovie
import pdm_1718i.yamda.model.Movie



class MoviesProvider {

    companion object {
        val SOURCE by lazy { TMDBService()}
    }

    fun searchMovies(query: String, page : Int, completionHandler: (movies: List<Movie>) -> Unit) {
        SOURCE.movieSearch(query, page, completionHandler)
    }


    fun movieDetail(id: Int, completionHandler: (movie : DetailedMovie) -> Unit){
        SOURCE.movieDetail(id, completionHandler)
    }
}