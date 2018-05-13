package pdm_1718i.yamda.data.db

import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import pdm_1718i.yamda.data.MoviesDataSource
import pdm_1718i.yamda.data.server.ImageOption
import pdm_1718i.yamda.extensions.toDetailedMovieItem
import pdm_1718i.yamda.extensions.toImage
import pdm_1718i.yamda.extensions.toMovieList
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.model.MovieDetail
import pdm_1718i.yamda.ui.App

class MoviesDb(private val provider : MoviesDataSource) : MoviesDataSource{

    override fun popularMovies(page: Int) : List<Movie>{
        val movieCursor = App.instance.contentResolver.query(MovieContract.MostPopularMovies.CONTENT_URI, MovieContract.MostPopularMovies.PROJECT_ALL, "${MovieContract.MovieListId.PAGE} = ?", arrayOf("$page"), null)
        Log.d("movieDB","returned ${movieCursor.count } items")
        return movieCursor.toMovieList() ?: provider.popularMovies(page)
    }

    override fun upcomingMovies(page: Int) : List<Movie>{
        val movieCursor = App.instance.contentResolver.query(MovieContract.UpcomingMovies.CONTENT_URI, MovieContract.UpcomingMovies.PROJECT_ALL, "${MovieContract.MovieListId.PAGE} = ?", arrayOf("$page"), null)
        Log.d("movieDB","returned ${movieCursor.count } items")
        return movieCursor.toMovieList() ?: provider.upcomingMovies(page)
    }

    override fun playingMovies (page: Int): List<Movie>{
        val movieCursor = App.instance.contentResolver.query(MovieContract.NowPlayingMovies.CONTENT_URI, MovieContract.NowPlayingMovies.PROJECT_ALL, "${MovieContract.MovieListId.PAGE} = ?", arrayOf("$page"), null)
        Log.d("movieDB","returned ${movieCursor.count } items")
        return movieCursor.toMovieList() ?: provider.playingMovies(page)
    }

    fun followingMovies (page : Int): List<Movie>{
        if(page > 1) return listOf()
        val movieCursor = App.instance.contentResolver.query(MovieContract.MovieDetails.CONTENT_URI, MovieContract.MovieDetails.PROJECT_ALL, "${MovieContract.MovieDetails.IS_FOLLOWING} = ?", arrayOf("1"), null)
        Log.d("movieDB","returned ${movieCursor.count } items")
        return movieCursor.toMovieList() ?: listOf()
    }

    override fun movieSearch(query: String, page : Int) :List<Movie>{
        return provider.movieSearch(query,page) //DATABASE does not deal with movie search
    }

    override fun movieDetail(id: Int) : MovieDetail {
        val movieCursor = App.instance.contentResolver.query(MovieContract.MovieDetails.CONTENT_URI, MovieContract.MovieDetails.PROJECT_ALL, "${MovieContract.MovieDetails._ID} = ?", arrayOf("$id"), null)
        Log.d("movieDB","returned ${movieCursor.count } items")
        val movie = movieCursor.toDetailedMovieItem()
        if(movie == null){
            val webResult = provider.movieDetail(id)
            App.instance.contentResolver
                    .insert(MovieContract.MovieDetails.CONTENT_URI, webResult.toContentValues())
            return webResult
        }
        return movie
    }

    override fun movieImage(image_id: String, imageView: ImageView, image_size: String){
        when(image_size){
            ImageOption.SMALL-> {
                val imageCursor = App.instance.contentResolver.query(MovieContract.Image.CONTENT_URI, MovieContract.Image.PROJECT_ALL, "${MovieContract.Image._ID} = ?", arrayOf(image_id), null)
                val bitmap = imageCursor.toImage()
                if (bitmap != null) imageView.setImageBitmap(bitmap)
                else provider.movieImage(image_id, imageView, image_size) //database does not deal with images
            }
            else -> provider.movieImage(image_id,imageView,image_size)
        }
    }

    override fun movieImage(image_id: String, image_size: String, bitmapCompletionHandler: (bitmap: Bitmap)-> Unit){
        when(image_size){
            ImageOption.SMALL-> {
                val imageCursor = App.instance.contentResolver.query(MovieContract.Image.CONTENT_URI, MovieContract.Image.PROJECT_ALL, "${MovieContract.Image._ID} = ?", arrayOf(image_id), null)
                val bitmap = imageCursor.toImage()
                if (bitmap != null) bitmapCompletionHandler(bitmap)
                else provider.movieImage(image_id, image_size, bitmapCompletionHandler) //database does not deal with images
            }
            else -> provider.movieImage(image_id,image_size, bitmapCompletionHandler)
        }
    }
    
        override fun movieImageSync(image_id: String, image_size: String): Bitmap {
        	return provider.movieImageSync(image_id, image_size)
        }

}