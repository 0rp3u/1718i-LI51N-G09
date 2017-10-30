package pdm_1718i.yamda.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import pdm_1718i.yamda.R
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App
import pdm_1718i.yamda.ui.adapters.SimplesMovieAdapter

class MovieListActivity : BaseListActivity(listView_id = R.id.list, emptyElement_id = R.id.emptyElement) {
    private var domain : List<Movie> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        val reqType  = intent.getStringExtra("requestType")
        title = reqType
            when (reqType) {
                "Now Playing" -> App.moviesProvider.nowPlayingMovies(1, { createGUI(it) })
                "Popular" -> App.moviesProvider.popularMovies(1, { createGUI(it) })
                "Upcoming" -> App.moviesProvider.upcomingMovies(1, { createGUI(it) })
                else -> Toast.makeText(App.instance, "Option cannot be processed. Try again.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun createGUI(movies : List< Movie>){
        if(movies.isNotEmpty()) {
            if(domain.isEmpty()) domain = movies
            listView.adapter = SimplesMovieAdapter(this, movies)
            listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val movieId = (listView.adapter.getItem(position) as Movie).id
                startActivity(Intent(applicationContext, MovieDetailActivity::class.java).putExtra("movieId", movieId))
            }
        }else{
            emptyView.visibility = View.VISIBLE
            listView.emptyView = emptyView
        }
    }


}
