package pdm_1718i.yamda.ui.activities

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.widget.ActionMenuView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text
import pdm_1718i.yamda.R
import pdm_1718i.yamda.model.DetailedMovie
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App

class MovieDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        val movieId = intent.getIntExtra("movieId", -1)
        if(movieId == -1)  Toast.makeText(App.instance, "Could not fetch Movie", Toast.LENGTH_SHORT).show()
        else App.moviesProvider.movieDetail(movieId, { updateUI(it) })
    }


    private fun updateUI(movieDetail: DetailedMovie) {
        val ratingTextView = findViewById(R.id.rating) as TextView
        val genreTextView = findViewById(R.id.genres) as TextView
        val overviewTextView = findViewById(R.id.overview) as TextView
        val iv = findViewById(R.id.movie_poster) as ImageView
        with(movieDetail){

            this@MovieDetailActivity.title = "$title($release_date)"

            ratingTextView.text = vote_average.toString()

            val genres = genres.joinToString(separator = ", ") {
                it.name
            }
            genreTextView.text = genres
            overviewTextView.text = overview

            if (poster_path != null && poster_path.isNotEmpty()) {
                App.moviesProvider.getImage(poster_path, iv)
            }


        }

    }

/*
            App.moviesProvider.getImage(poster_path_1,{
                val image_view_1 = findViewById(R.id.popular_image_1) as ImageView
                image_view_1.setImageBitmap(it)
 */
}