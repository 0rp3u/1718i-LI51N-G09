package pdm_1718i.yamda.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.server.TMDBService
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.adapters.EndlessAdapter
import pdm_1718i.yamda.ui.adapters.EndlessListener
import pdm_1718i.yamda.ui.holders.EndlessListView

open class BaseListActivity(actionBar: Boolean = true, listView_id: Int, emptyElement_id: Int) : BaseActivity(actionBar), EndlessListener{

    private val MOVIE_KEY = "movieId"

    private val listView: EndlessListView by lazy { findViewById<EndlessListView>(listView_id) }
    private val emptyView: TextView by lazy { findViewById<TextView>(emptyElement_id) }
    private var CURRENT_PAGE: Int = TMDBService.DEFAULT_PAGINATION

    protected lateinit var movieListProvider: (Int) -> List<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        listView.setFooterView(R.layout.loading_layout)
    }


    protected fun createGUI(movies: List<Movie>) {
        if (movies.isNotEmpty()) {
            listView.setAdapter(EndlessAdapter(this, movies))
            listView.setListener(this)

            listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val item = listView.adapterEndless.getItem(position)
                val movieId = item.id
                with(Intent(applicationContext, MovieDetailActivity::class.java).putExtra(MOVIE_KEY, movieId)){
                    startActivity(this)
                }
            }
        } else{
            emptyView.visibility = View.VISIBLE
            listView.emptyView = emptyView
        }
    }

    override fun loadData() {
        if (CURRENT_PAGE > 0) {
            Log.d("recycler", "loading page ${CURRENT_PAGE + 1}")
            movieListProvider.let {
                async(UI) {
                    try {
                        val result = bg {
                            it(++CURRENT_PAGE)
                        }
                        updateGUI(result.await())
                    } catch (e: Exception) {
                        Log.d("asynExecption", "${e.message}")
                        listView.setProblem("something went wrong! click to try again")
                    }
                }
            }
        }
    }


    private fun updateGUI(movies: List<Movie>) {
        if (movies.isNotEmpty()) {

            listView.addNewData(movies)
        } else {
            CURRENT_PAGE = -1 //so user does not make calls for unavailable pages
            listView.setFull()
        }
    }
}