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
import pdm_1718i.yamda.extensions.caseFalse
import pdm_1718i.yamda.extensions.runIf
import pdm_1718i.yamda.extensions.toast
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App
import pdm_1718i.yamda.ui.App.Companion.isNetworkAvailable
import pdm_1718i.yamda.ui.adapters.MainActAdapter

class MainActivity : BaseActivity() {
    private val REQUEST_TYPE = "requestType"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        runIf(isNetworkAvailable) {
            generateRecyclerView(R.id.recycler_view_nowplaying, App.moviesProvider::nowPlayingMovies)
            generateRecyclerView(R.id.recycler_view_upcoming,   App.moviesProvider::upcomingMovies)
            generateRecyclerView(R.id.recycler_view_popular,    App.moviesProvider::popularMovies)
        }.caseFalse { toast(NO_INTERNET_CONNECTION) }
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

    private fun generateRecyclerView(
            res: Int,
            providerHandler: (page:Int, completionHandler:(movies:List<Movie>) -> Unit)-> Unit
    ){
        with(findViewById(res) as RecyclerView){
            this.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayout.HORIZONTAL, false)
            providerHandler(DEFAULT_PAGINATION, {
                val adapter = MainActAdapter(it)
                this.adapter = adapter
            })
        }
    }
}
