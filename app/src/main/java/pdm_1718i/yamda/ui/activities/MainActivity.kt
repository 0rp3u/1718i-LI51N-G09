package pdm_1718i.yamda.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.server.TMDBService.Companion.DEFAULT_PAGINATION
import pdm_1718i.yamda.extensions.NO_INTERNET_CONNECTION
import pdm_1718i.yamda.extensions.caseTrue
import pdm_1718i.yamda.extensions.runIf
import pdm_1718i.yamda.extensions.toast
import pdm_1718i.yamda.ui.App
import pdm_1718i.yamda.ui.App.Companion.isNetworkAvailable
import pdm_1718i.yamda.ui.adapters.MainActAdapter

class MainActivity : BaseActivity() {
    private val REQUEST_TYPE = "requestType"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        runIf(isNetworkAvailable) {
            val recyclerViewNowPlaying = findViewById(R.id.recycler_view_nowplaying) as RecyclerView
            recyclerViewNowPlaying.layoutManager = LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)
            App.moviesProvider.nowPlayingMovies(DEFAULT_PAGINATION, {
                val adapter = MainActAdapter(it)
                recyclerViewNowPlaying.adapter = adapter
            })

            val recyclerViewUpcoming = findViewById(R.id.recycler_view_upcoming) as RecyclerView
            recyclerViewUpcoming.layoutManager = LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)
            App.moviesProvider.upcomingMovies(DEFAULT_PAGINATION, {
                val adapter = MainActAdapter(it)
                recyclerViewUpcoming.adapter = adapter
            })

            val recyclerViewPopular = findViewById(R.id.recycler_view_popular) as RecyclerView
            recyclerViewPopular.layoutManager = LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)
            App.moviesProvider.popularMovies(DEFAULT_PAGINATION, {
                val adapter = MainActAdapter(it)
                recyclerViewPopular.adapter = adapter
            })
        }.not().caseTrue { toast(NO_INTERNET_CONNECTION) }
    }

    fun onPopularMore(view: View){
        with(Intent(applicationContext, MovieListActivity::class.java)){
            putExtra(REQUEST_TYPE, "Popular")
            startActivity(this)
        }
    }

    fun onInTheatersMore(view: View){
        with(Intent(applicationContext, MovieListActivity::class.java)){
            putExtra(REQUEST_TYPE, "Now Playing")
            startActivity(this)
        }
    }

    fun onUpcomingMore(view: View){
        with(Intent(applicationContext, MovieListActivity::class.java)){
            putExtra(REQUEST_TYPE, "Upcoming")
            startActivity(this)
        }
    }
}
