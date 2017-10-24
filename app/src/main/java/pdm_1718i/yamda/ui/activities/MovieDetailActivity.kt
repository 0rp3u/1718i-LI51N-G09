package pdm_1718i.yamda.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.Toast
import pdm_1718i.yamda.R
import pdm_1718i.yamda.ui.App

class MovieDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        val movieId = intent.getIntExtra("movieId", -1)
        App.moviesProvider.movieDetail(movieId, {
            App.moviesProvider.getImage(it.poster_path, {
                var iv = ImageView(applicationContext)
                iv.setImageBitmap(it)
            })
            Toast.makeText(this, it.title, Toast.LENGTH_SHORT).show()
        })
    }
}
