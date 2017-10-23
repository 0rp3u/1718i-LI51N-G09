package pdm_1718i.yamda.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import pdm_1718i.yamda.R
import pdm_1718i.yamda.ui.App

class MovieDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        val movieId = intent.getIntExtra("movieId", -1)
            App.moviesProvider.movieDetail(movieId, {

                Toast.makeText(this, it.title, Toast.LENGTH_SHORT).show()
            })

    }
}
