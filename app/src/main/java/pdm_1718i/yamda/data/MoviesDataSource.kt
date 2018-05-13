package pdm_1718i.yamda.data

import android.graphics.Bitmap
import android.widget.ImageView
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.model.MovieDetail

interface MoviesDataSource {

    fun popularMovies(page: Int) : List<Movie>
    fun upcomingMovies(page: Int) : List<Movie>
    fun playingMovies(page: Int) : List<Movie>
    fun movieSearch(query: String, page: Int) : List<Movie>
    fun movieDetail(id: Int) : MovieDetail
    fun movieImage(image_id: String, imageView: ImageView, image_size: String)
    fun movieImage(image_id: String, image_size: String, bitmapCompletionHandler: (bitmap: Bitmap)-> Unit) : Any?
    fun movieImageSync(image_id: String, image_size: String): Bitmap
}