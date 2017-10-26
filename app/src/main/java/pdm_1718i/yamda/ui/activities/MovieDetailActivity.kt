package pdm_1718i.yamda.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.server.Options
import pdm_1718i.yamda.model.DetailedMovie
import pdm_1718i.yamda.ui.App

class MovieDetailActivity : AppCompatActivity(), ToolbarManager{

    override val toolbar by lazy{ findViewById(R.id.toolbar) as Toolbar}
    val release_date by lazy { findViewById(R.id.release_date) as TextView }
    val ratingTextView by lazy {findViewById(R.id.rating) as TextView }
    val genreTextView by lazy {findViewById(R.id.genres) as TextView }
    val overviewTextView  by lazy { findViewById(R.id.overview) as TextView }
    val iv  by lazy { findViewById(R.id.movie_poster) as ImageView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        val movieId = intent.getIntExtra("movieId", -1)
        if(movieId == -1)  Toast.makeText(App.instance, "Could not fetch Movie", Toast.LENGTH_SHORT).show()
        else App.moviesProvider.movieDetail(movieId, { updateUI(it) })
        initToolbar()

    }

    private fun updateUI(movieDetail: DetailedMovie) {
        with(movieDetail){
            this@MovieDetailActivity.toolbarTitle = "$title (${release_date.substring(0, 4)})"
            this@MovieDetailActivity.release_date.text = release_date
            ratingTextView.text = vote_average.toString()
            val genres = genres.joinToString(separator = ", ") {it.name}
            genreTextView.text = genres
            overviewTextView.text = overview

            if (poster_path != null && poster_path.isNotEmpty()) {

                App.moviesProvider.image(poster_path, iv, Options.poster_sizes["BIG"]!!)
            }
        }
    }


}