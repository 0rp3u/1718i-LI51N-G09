package pdm_1718i.yamda.data.db

import android.graphics.Bitmap
import android.widget.ImageView
import pdm_1718i.yamda.data.MoviesDataSource
import pdm_1718i.yamda.model.DetailedMovie
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App
/*
class MoviesDb (private val provider : MoviesDataSource): MoviesDataSource{

    override fun popularMovies(page: Int, completionHandler: (movies: List<Movie>) -> Unit){
        provider.popularMovies(page,completionHandler)
    }

    override fun upcomingMovies(page: Int, completionHandler: (movies: List<Movie>) -> Unit){
        val movieCursor = App.instance.contentResolver.query(MovieContract.UpcomingMovies.CONTENT_URI, null, null, null, null)

        if (movieCursor.count > 0) {
            movieCursor.moveToFirst()
            val movies : MutableList<Movie>
            while(!movieCursor.isAfterLast())
                movies.add(Movie(movieCursor.getString(MovieContract.MovieDetails)))

                //Gather values
                String id = c . getString (1);
                String name = c . getString (iname);
                String isbn = c . getString (iisbn);

            }
                movieCursor.moveToNext()
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