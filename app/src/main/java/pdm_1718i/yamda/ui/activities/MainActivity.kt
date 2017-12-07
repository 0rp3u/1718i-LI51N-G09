package pdm_1718i.yamda.ui.activities

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.annotation.UiThread
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
import pdm_1718i.yamda.ui.activities.MovieListActivity.Companion.PLAYING
import pdm_1718i.yamda.ui.activities.MovieListActivity.Companion.POPULAR
import pdm_1718i.yamda.ui.activities.MovieListActivity.Companion.REQUEST_TYPE
import pdm_1718i.yamda.ui.activities.MovieListActivity.Companion.UPCOMING
import pdm_1718i.yamda.ui.adapters.MainActAdapter

class MainActivity : BaseActivity(navigation = false) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        generateRecyclerViewTask(R.id.recycler_view_nowplaying, App.moviesProvider::nowPlayingMovies)
        generateRecyclerViewTask(R.id.recycler_view_upcoming,   App.moviesProvider::upcomingMovies)
        generateRecyclerViewTask(R.id.recycler_view_popular,    App.moviesProvider::popularMovies)

        Log.d("Main_on_create", "finisehd on Create")
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

    /*
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
    }*/



    private fun generateRecyclerViewTask(res: Int, providerHandler: (page:Int) -> List<Movie>) {
        async(UI) {
            val result = bg{
                Log.d("reciclerViewTask", "trow new background thread: ${Thread.currentThread().id} to fetch data")
                providerHandler(DEFAULT_PAGINATION)
            }

            val resView = findViewById<RecyclerView>(res)
            resView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayout.HORIZONTAL, false)
            val adapter = MainActAdapter(result.await())
            Log.d("reciclerViewTask", "bg thread retured execution to UI thread:${Thread.currentThread().id}")
            resView.adapter = adapter
        }


    }
/*
        private fun generateRecyclerViewTask(res: Int, providerHandler: (page:Int) -> List<Movie>) {

            (object: AsyncTask<String, Unit, List<Movie>>() {
            override fun doInBackground(vararg p0: String?): List<Movie> {
                Log.d("reciclerViewTask", "doInBackground in ${Thread.currentThread().id}")
                return  providerHandler(DEFAULT_PAGINATION)

            }
            override fun onPostExecute(result: List<Movie>) {
                val resView = findViewById<RecyclerView>(res)
                resView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayout.HORIZONTAL, false)
                    val adapter = MainActAdapter(result)
                    resView.adapter = adapter
                Log.d("reciclerViewTask", "onPostExecute in ${Thread.currentThread().id}")

            }
        }).execute()
    }*/
}
