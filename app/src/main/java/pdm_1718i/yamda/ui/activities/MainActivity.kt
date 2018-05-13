package pdm_1718i.yamda.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.server.TMDBService.Companion.DEFAULT_PAGINATION
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App
import pdm_1718i.yamda.ui.activities.MovieListActivity.Companion.FOLLOWING
import pdm_1718i.yamda.ui.activities.MovieListActivity.Companion.PLAYING
import pdm_1718i.yamda.ui.activities.MovieListActivity.Companion.POPULAR
import pdm_1718i.yamda.ui.activities.MovieListActivity.Companion.REQUEST_TYPE
import pdm_1718i.yamda.ui.activities.MovieListActivity.Companion.UPCOMING
import pdm_1718i.yamda.ui.adapters.MainActAdapter

class MainActivity : BaseActivity(navigation = false) {

    private val nowPlayingView  by lazy { findViewById<RecyclerView>(R.id.recycler_view_nowplaying) }
    private val upcomingView    by lazy { findViewById<RecyclerView>(R.id.recycler_view_upcoming)   }
    private val popularView     by lazy { findViewById<RecyclerView>(R.id.recycler_view_popular)    }
    private val followingView   by lazy { findViewById<RecyclerView>(R.id.recycler_view_following)  }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        generateRecyclerView(nowPlayingView)
        generateRecyclerView(upcomingView)
        generateRecyclerView(popularView)
        generateRecyclerView(followingView)
    }

    override fun onResume() {
        super.onResume()
        updateRecyclerViewTask(nowPlayingView, App.moviesProvider::nowPlayingMovies)
        updateRecyclerViewTask(upcomingView,   App.moviesProvider::upcomingMovies)
        updateRecyclerViewTask(popularView,    App.moviesProvider::popularMovies)
        updateRecyclerViewTask(followingView,  App.moviesProvider::followingMovies)
    }

    fun onPopularMore(view: View){
        with(Intent(applicationContext, MovieListActivity::class.java)){
            putExtra(REQUEST_TYPE, POPULAR)
            startActivity(this)
        }
    }

    fun onInTheatersMore(view: View){
        with(Intent(applicationContext, MovieListActivity::class.java)){
            putExtra(REQUEST_TYPE, PLAYING)
            startActivity(this)
        }
    }

    fun onUpcomingMore(view: View){
        with(Intent(applicationContext, MovieListActivity::class.java)){
            putExtra(REQUEST_TYPE, UPCOMING)
            startActivity(this)
        }
    }

    fun onFollowingMore(view: View){
        with(Intent(applicationContext, MovieListActivity::class.java)){
            putExtra(REQUEST_TYPE, FOLLOWING)
            startActivity(this)
        }
    }


    private fun generateRecyclerView(resView : RecyclerView) {
        resView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayout.HORIZONTAL, false)
        resView.adapter = MainActAdapter(listOf())
    }

    private fun updateRecyclerViewTask(resView : RecyclerView, providerHandler: (page:Int) -> List<Movie>) {
        val adapter = resView.adapter as MainActAdapter
        async(UI) {
            try{
                val results = bg{ providerHandler(DEFAULT_PAGINATION) }
                adapter.setData(results.await())
            }catch (e: Exception){
                Log.d("Exeption", "${e.message}")
            }
        }
    }
}
