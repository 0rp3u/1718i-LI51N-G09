package pdm_1718i.yamda.ui.activities

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
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

class SearchResultActivity: BaseListActivity(listView_id = R.id.list, emptyElement_id = R.id.emptyElement), EndlessListView.EndlessListener {

    private val MOVIE_KEY = "movieId"
    private val RESULT_TITLE: String = App.instance.getString(R.string.results_for)
    private var CURRENT_PAGE : Int = DEFAULT_PAGINATION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        listView.setLoadingView(R.layout.loading_layout)
        val query = intent.getStringExtra(QUERY_KEY)
        title = "$RESULT_TITLE: $query"
        async(UI) {
            val movies = bg{App.moviesProvider.searchMovies(query, DEFAULT_PAGINATION)}
            createGUI(movies.await())
        }
    }


    override fun loadData() {
        if(CURRENT_PAGE > 0) {
            val query = intent.getStringExtra(QUERY_KEY)
            async (UI){  updateGUI(bg{App.moviesProvider.searchMovies(query, ++CURRENT_PAGE)}.await())}


        }
    }

    private fun createGUI(movies: List<Movie>) {
        if (movies.isNotEmpty()) {
            listView.setAdapter(EndlessAdapter(this, movies))
            listView.setListener(this)

            listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val movieId = (listView.adapter.getItem(position) as Movie).id
                with(Intent(applicationContext, MovieDetailActivity::class.java).putExtra(MOVIE_KEY, movieId)){
                    startActivity(this)
                }
            }
        } else{
            emptyView.visibility = View.VISIBLE
            listView.emptyView = emptyView
        }
    }

    private fun updateGUI(movies : List< Movie>) {
        if(movies.isNotEmpty()) {
            listView.addNewData(movies)
        }else{
           CURRENT_PAGE = -1 //so user does not make calls for unavailable pages
        }


    }
}