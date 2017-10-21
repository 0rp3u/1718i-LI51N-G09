package pdm_1718i.yamda.data

import pdm_1718i.yamda.model.Movie

interface MoviesDataSource {

    fun movieSearch(query: String, page : Int, completionHandler: (movies: List<Movie>) -> Unit)

}