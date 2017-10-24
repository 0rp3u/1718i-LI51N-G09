package pdm_1718i.yamda.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text
import pdm_1718i.yamda.R
import pdm_1718i.yamda.ui.App

class MovieDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        val movieId = intent.getIntExtra("movieId", -1)
        App.moviesProvider.movieDetail(movieId, {
            val titleTextView = findViewById(R.id.movie_title) as TextView
            titleTextView.text = it.title
            val releasedateTextView = findViewById(R.id.release_date) as TextView
            releasedateTextView.text=it.release_date
            val ratingTextView = findViewById(R.id.rating) as TextView
            ratingTextView.text = it.vote_average.toString()

            val genres = it.genres.map {
                it.name
            }.joinToString(separator=",")

            var genreTextView = findViewById(R.id.genres) as TextView
            genreTextView.text = genres

            val overviewTextView = findViewById(R.id.overview) as TextView
            overviewTextView.text=it.overview

            if(it.poster_path != null && it.poster_path.isNotEmpty()){
                App.moviesProvider.getImage(it.poster_path, {
                    var iv = findViewById(R.id.movie_poster) as ImageView
                    iv.setImageBitmap(it)
                })
            }

            Toast.makeText(this, it.title, Toast.LENGTH_SHORT).show()
        })
    }
}

/*
            App.moviesProvider.getImage(poster_path_1,{
                val image_view_1 = findViewById(R.id.popular_image_1) as ImageView
                image_view_1.setImageBitmap(it)
 */
