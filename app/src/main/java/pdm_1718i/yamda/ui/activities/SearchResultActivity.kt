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
import pdm_1718i.yamda.data.server.TMDBService2.Companion.DEFAULT_PAGINATION
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

    override fun loadData() {
        val query = intent.getStringExtra(QUERY_KEY)
        async(UI) { createGUI(bg{App.moviesProvider.searchMovies(query, ++CURRENT_PAGE)}.await())}
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val query = intent.getStringExtra(QUERY_KEY)
        title = "$RESULT_TITLE: $query"
        async(UI) {
            val movies = bg{App.moviesProvider.searchMovies(query, DEFAULT_PAGINATION)}
            createGUI(movies.await())
        }
    }

    private fun createGUI(movies: List<Movie>) {
        if (movies.isNotEmpty()) {

            if(listView.adapter==null)
                listView.setAdapter(EndlessAdapter(this, movies))
            else
                listView.addNewData(movies)

            if (listView.getListener()==null)
                listView.setListener(this)

            listView.setLoadingView(R.layout.loading_layout)

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
}