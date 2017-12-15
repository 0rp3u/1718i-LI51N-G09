package pdm_1718i.yamda.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.server.TMDBService.Companion.DEFAULT_PAGINATION
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App
import pdm_1718i.yamda.ui.adapters.EndlessAdapter
import pdm_1718i.yamda.ui.holders.EndlessListView

class MovieListActivity : BaseListActivity(listView_id = R.id.list, emptyElement_id = R.id.emptyElement), EndlessListView.EndlessListener {

    companion object {
        private val MOVIE_KEY = "movieId"
        val REQUEST_TYPE = "requestType"
        val POPULAR = "Popular"
        val PLAYING = "Now Playing"
        val UPCOMING = "Upcoming"
        @JvmStatic val dispatcher = mapOf<String, (Int) -> List<Movie>>(
                POPULAR     to App.moviesProvider::popularMovies,
                        PLAYING     to App.moviesProvider::nowPlayingMovies,
                        UPCOMING    to App.moviesProvider::upcomingMovies
        )

        private var CURRENT_PAGE : Int = DEFAULT_PAGINATION
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        with(intent.getStringExtra(REQUEST_TYPE)){
            title = this
            dispatcher[this]?.let {
                async(UI) { createGUI(bg{it(DEFAULT_PAGINATION)}.await()) }
            }
        }
    }

    override fun loadData() {
        if(CURRENT_PAGE > 0) {
            Log.d("recicler", "loading page ${CURRENT_PAGE+1}")
            with(intent.getStringExtra(REQUEST_TYPE)) {
                dispatcher[this]?.let {
                    async(UI) { updateGUI(bg { it(++CURRENT_PAGE) }.await()) }
                }
            }
        }
    }

    private fun updateGUI(movies : List< Movie>) {
        if(movies.isNotEmpty()) {
            Log.d("recicler", "finished loading pages")

            listView.addNewData(movies)
        }else{
            Log.d("recicler", "no more pages to load")

            CURRENT_PAGE = -1 //so user does not make calls for unavailable pages
            listView.setFull()
        }


    }

    private fun createGUI(movies : List< Movie>){
        if(movies.isNotEmpty()) {
            listView.setAdapter(EndlessAdapter(this, movies))
            listView.setListener(this)
            listView.setFooterView(R.layout.loading_layout)

            listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val item = listView.adapter.getItem(position)
                if(item != null){
                    val movieId = (item as Movie).id
                    startActivity(Intent(applicationContext, MovieDetailActivity::class.java).putExtra(MOVIE_KEY, movieId))
                }
            }
        }else{
            emptyView.visibility = View.VISIBLE
            listView.emptyView = emptyView
        }
    }
}
