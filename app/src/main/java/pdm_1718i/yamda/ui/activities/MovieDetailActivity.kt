package pdm_1718i.yamda.ui.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.server.Options
import pdm_1718i.yamda.model.DetailedMovie
import pdm_1718i.yamda.ui.App

class MovieDetailActivity : ChildActivity() {

    private val release_date by lazy { findViewById(R.id.release_date) as TextView }
    private val ratingTextView by lazy {findViewById(R.id.rating) as TextView }
    private val genreTextView by lazy {findViewById(R.id.genres) as TextView }
    private val overviewTextView  by lazy { findViewById(R.id.overview) as TextView }
    private val imageView by lazy { findViewById(R.id.movie_poster) as ImageView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        val movieId = intent.getIntExtra("movieId", -1)
        if(movieId == -1)  Toast.makeText(App.instance, "Could not fetch Movie", Toast.LENGTH_SHORT).show()
        else App.moviesProvider.movieDetail(movieId, { updateUI(it) })

    }

    private fun updateUI(movieDetail: DetailedMovie) {
        with(movieDetail){
            this@MovieDetailActivity.title = "$title (${release_date.substring(0, 4)})"
            this@MovieDetailActivity.release_date.text = release_date
            ratingTextView.text = vote_average.toString()
            val genres = genres.joinToString(separator = ", ") {it.name}
            genreTextView.text = genres
            overviewTextView.text = overview

            if (poster_path != null && poster_path.isNotEmpty()) {
                App.moviesProvider.image(poster_path, imageView, Options.poster_sizes["BIG"]!!)
            }
        }
    }


}