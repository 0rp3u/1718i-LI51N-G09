package pdm_1718i.yamda.ui.activities

import android.content.Intent
import android.os.Bundle
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.server.TMDBService.Companion.DEFAULT_PAGINATION
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App

class SearchResultActivity: BaseListActivity(listView_id = R.id.list, emptyElement_id = R.id.emptyElement) {

    private val RESULT_TITLE: String = App.instance.getString(R.string.results_for)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val curPage = savedInstanceState?.getInt(super.CURRENT_PAGE_KEY, 1) ?: 1
        val curPos = savedInstanceState?.getInt(super.POSITION_KEY, 0)  ?: 0
        handleIntent(intent, curPage, curPos)
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent, 1, 0)
    }

    private fun handleIntent(intent: Intent, curPage : Int, curPos : Int) {
        val query = intent.getStringExtra(QUERY_KEY)

        title = "$RESULT_TITLE: $query"
        movieListProvider = { page :Int-> App.moviesProvider.searchMovies(query, page) }
        async(UI) {
            super.createGUI(
                    try { bg { loadPages(1, curPage) }.await() } catch (e: Exception) { listOf<Movie>() }
            , curPos)
        }
    }

}