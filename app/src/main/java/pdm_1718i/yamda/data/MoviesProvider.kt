package pdm_1718i.yamda.data

import pdm_1718i.yamda.data.server.TMDBService
import pdm_1718i.yamda.model.Movie



class MoviesProvider(private val sources: List<MoviesDataSource> = MoviesProvider.SOURCES) {

    companion object {
        val SOURCES by lazy { listOf( TMDBService()) }
    }

    fun searchMovies(query: String, page : Int, completionHandler: (movies: List<Movie>) -> Unit) {

        sources.first().movieSearch(query, page, completionHandler)
    }

}