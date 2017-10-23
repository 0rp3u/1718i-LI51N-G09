package pdm_1718i.yamda.data

import pdm_1718i.yamda.model.DetailedMovie
import pdm_1718i.yamda.model.Movie

interface MoviesDataSource {

    fun popularSearch(page: Int, completionHandler: (movies: List<Movie>) -> Unit)
    fun upcomingSearch(query: String, page: Int, completionHandler: (movies: List<Movie>) -> Unit)
    fun playingSearch (query: String, page: Int, completionHandler: (movies: List<Movie>) -> Unit)
    fun movieSearch(query: String, page : Int, completionHandler: (movies: List<Movie>) -> Unit)
    fun movieDetail(id: Int, completionHandler: (movies: DetailedMovie) -> Unit)

}