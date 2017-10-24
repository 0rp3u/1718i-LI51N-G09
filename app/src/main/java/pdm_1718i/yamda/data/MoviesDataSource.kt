package pdm_1718i.yamda.data

import android.graphics.Bitmap
import pdm_1718i.yamda.model.DetailedMovie
import pdm_1718i.yamda.model.Movie

interface MoviesDataSource {

    fun popularSearch(page: Int, completionHandler: (movies: List<Movie>) -> Unit)
    fun upcomingSearch(query: String, page: Int, completionHandler: (movies: List<Movie>) -> Unit)
    fun playingSearch (query: String, page: Int, completionHandler: (movies: List<Movie>) -> Unit)
    fun movieSearch(query: String, page : Int, completionHandler: (movies: List<Movie>) -> Unit)
    fun movieDetail(id: Int, completionHandler: (movies: DetailedMovie) -> Unit)

    fun movieImage(image_id: String, completionHandler: (image: Bitmap) -> Unit, image_size: String)
}