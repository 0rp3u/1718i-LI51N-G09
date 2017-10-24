package pdm_1718i.yamda.data

import android.graphics.Bitmap
import pdm_1718i.yamda.model.DetailedMovie
import pdm_1718i.yamda.model.Movie

interface MoviesDataSource {

    fun popularMovies(page: Int, completionHandler: (movies: List<Movie>) -> Unit)
    fun upcomingMovies(page: Int, completionHandler: (movies: List<Movie>) -> Unit)
    fun playingMovies (page: Int, completionHandler: (movies: List<Movie>) -> Unit)
    fun movieSearch(query: String, page : Int, completionHandler: (movies: List<Movie>) -> Unit)
    fun movieDetail(id: Int, completionHandler: (movies: DetailedMovie) -> Unit)

    fun movieImage(image_id: String, completionHandler: (image: Bitmap) -> Unit, image_size: String)
}