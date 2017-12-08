package pdm_1718i.yamda.data.db

import android.graphics.Bitmap
import android.widget.ImageView
import pdm_1718i.yamda.data.MoviesDataSource
import pdm_1718i.yamda.model.DetailedMovie
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App
import java.util.*
/*
class MoviesDb (private val provider : MoviesDataSource): MoviesDataSource{

    override fun popularMovies(page: Int, completionHandler: (movies: List<Movie>) -> Unit){
        provider.popularMovies(page,completionHandler)
    }

    override fun upcomingMovies(page: Int, completionHandler: (movies: List<Movie>) -> Unit){
        val movieCursor = App.instance.contentResolver.query(MovieContract.UpcomingMovies.CONTENT_URI, null, null, null, null)

        if (movieCursor.count > 0) {

            val movieId = movieCursor.getColumnIndex(MovieContract.MovieDetails._ID)
            val movieTitle = movieCursor.getColumnIndex(MovieContract.MovieDetails.TITLE)
            val movieReleaseDate = movieCursor.getColumnIndex(MovieContract.MovieDetails.RELEASE_DATE)
            val movieBackdropPath = movieCursor.getColumnIndex(MovieContract.MovieDetails.POSTER_PATH)
            val movieIsFollowing= movieCursor.getColumnIndex(MovieContract.MovieDetails.IS_FOLLOWING)


            val poster_path : String,
            val release_date : Calendar?,
            val id: Int,
            val title : String,
            val backdrop_path : String,
            val vote_average: Double

            val movies : MutableList<Movie>
            movieCursor.moveToFirst()
            while(!movieCursor.isAfterLast()){
                movies.add(
                            movieCursor.getString(movieBackdropPath),
                            movieCursor.getString(movieReleaseDate)
                            movieCursor.getString(movieId)
                        )

                //Gather values
                String id = c . getString (1);
                String name = c . getString (iname);
                String isbn = c . getString (iisbn);



                movieCursor.moveToNext()
            }

        }
    }

    override fun playingMovies (page: Int, completionHandler: (movies: List<Movie>) -> Unit){


    }

    override fun movieSearch(query: String, page : Int, completionHandler: (movies: List<Movie>) -> Unit){


    }

    override fun movieDetail(id: Int, completionHandler: (movies: DetailedMovie) -> Unit){

    }

    override fun movieImage(image_id: String, imageView: ImageView, image_size: String){


    }

    override fun movieImage(image_id: String, bitmapCompletionHandler: (bitmap: Bitmap)-> Unit, image_size: String){



    }


}*/