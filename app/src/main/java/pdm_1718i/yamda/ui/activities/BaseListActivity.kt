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
    protected val POSITION_KEY = "listPosition"
    protected val CURRENT_PAGE_KEY = "currentPage"

    private val listView: EndlessListView by lazy { findViewById<EndlessListView>(listView_id) }
    private val emptyView: TextView by lazy { findViewById<TextView>(emptyElement_id) }
    private var CURRENT_PAGE: Int = TMDBService.DEFAULT_PAGINATION

    protected lateinit var movieListProvider: (Int) -> List<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        listView.setFooterView(R.layout.loading_layout)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val index = listView.firstVisiblePosition
        outState.putInt(CURRENT_PAGE_KEY, CURRENT_PAGE)
        outState.putInt(POSITION_KEY, index)
        super.onSaveInstanceState(outState)
    }

    protected fun createGUI(movies: List<Movie>, lisPos: Int) {
        if (movies.isNotEmpty()) {
            listView.setAdapter(EndlessAdapter(this, movies))
            listView.setListener(this)

            listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                if(listView.adapterEndless.getItemCount() > position){
                    val movieId = (listView.adapterEndless.getItem(position)).id
                    with(Intent(applicationContext, MovieDetailActivity::class.java).putExtra(MOVIE_KEY, movieId)){
                        startActivity(this)
                    }
                }
            }
            listView.setSelection(lisPos)
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
                        Log.d("loadDataException", "${e.message}")
                        listView.setProblem("something went wrong! click to try again")
                    }
                }
            }
        }
    }

    protected fun loadPages(fromPage:  Int, toPage : Int): List<Movie> = //synchronous, used for on restore, when we know that data is probably cached
        (fromPage..toPage).fold(mutableListOf()) {
            acc: MutableList<Movie>, next -> run {
                acc.addAll(movieListProvider(next))
            }
            CURRENT_PAGE = toPage
            acc
        }


    private fun updateGUI(movies: List<Movie>) {
        if (movies.isNotEmpty()) {
            listView.addNewData(movies)
        } else {
            listView.setFull()
        }
    }
}