package pdm_1718i.yamda.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.server.TMDBService.Companion.DEFAULT_PAGINATION
import pdm_1718i.yamda.extensions.NO_INTERNET_CONNECTION
import pdm_1718i.yamda.extensions.caseFalse
import pdm_1718i.yamda.extensions.runIf
import pdm_1718i.yamda.extensions.toast
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App
import pdm_1718i.yamda.ui.App.Companion.isNetworkAvailable
import pdm_1718i.yamda.ui.adapters.SimpleMovieAdapter

class MovieListActivity : BaseListActivity(listView_id = R.id.list, emptyElement_id = R.id.emptyElement) {

    companion object {
        private val MOVIE_KEY = "movieId"
        val REQUEST_TYPE = "requestType"
        val POPULAR = "Popular"
        val PLAYING = "Now Playing"
        val UPCOMING = "Upcoming"
        @JvmStatic val dispatcher = mapOf<String, (Int, (List<Movie>) -> Unit) -> Unit>(
                POPULAR     to App.moviesProvider::popularMovies,
                PLAYING     to App.moviesProvider::nowPlayingMovies,
                UPCOMING    to App.moviesProvider::upcomingMovies
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        runIf(isNetworkAvailable){
            with(intent.getStringExtra(REQUEST_TYPE)){
                title = this
                dispatcher[this]?.let { it(DEFAULT_PAGINATION, { createGUI(it) }) }
            }
        }.caseFalse { toast(NO_INTERNET_CONNECTION) }
    }

    private fun createGUI(movies : List< Movie>){
        if(movies.isNotEmpty()) {
            listView.adapter = SimpleMovieAdapter(this, movies)
            listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val movieId = (listView.adapter.getItem(position) as Movie).id
                startActivity(Intent(applicationContext, MovieDetailActivity::class.java).putExtra(MOVIE_KEY, movieId))
            }
        }else{
            emptyView.visibility = View.VISIBLE
            listView.emptyView = emptyView
        }
    }
}
