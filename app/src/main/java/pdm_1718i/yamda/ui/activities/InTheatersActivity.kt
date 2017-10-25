package pdm_1718i.yamda.ui.activities

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Toast
import pdm_1718i.yamda.R
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App
import pdm_1718i.yamda.ui.adapters.SimplesMovieAdapter

class InTheatersActivity : ListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_theaters)
        val reqType  = intent.getStringExtra("requestType")
        when(reqType){
            "NowPlaying" ->  App.moviesProvider.nowPlayingMovies(1, { createGUI(it)})
            "Popular" ->  App.moviesProvider.popularMovies(1, { createGUI(it)})
            "Upcoming" ->  App.moviesProvider.upcomingMovies(1, { createGUI(it)})
            else -> Toast.makeText(App.instance, "Something Went KABOOM", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createGUI(movies : List< Movie>){
            listView.adapter = SimplesMovieAdapter(this, movies)
            listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val movieId = (listView.adapter.getItem(position) as Movie).id
                startActivity(Intent(applicationContext, MovieDetailActivity::class.java).putExtra("movieId", movieId))
            }
    }
}
