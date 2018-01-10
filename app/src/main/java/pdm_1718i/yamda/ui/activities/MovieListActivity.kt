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

class MovieListActivity : BaseListActivity(listView_id = R.id.list, emptyElement_id = R.id.emptyElement) {

    companion object {
        val REQUEST_TYPE = "requestType"
        val POPULAR = "Popular"
        val PLAYING = "Now Playing"
        val UPCOMING = "Upcoming"
        val FOLLOWING = "Following"
        @JvmStatic val dispatcher = mapOf<String, (Int) -> List<Movie>>(
                POPULAR     to App.moviesProvider::popularMovies,
                PLAYING     to App.moviesProvider::nowPlayingMovies,
                UPCOMING    to App.moviesProvider::upcomingMovies,
                FOLLOWING   to App.moviesProvider::followingMovies
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val curPage = savedInstanceState?.getInt(super.CURRENT_PAGE_KEY, 1) ?: 1
        val curPos = savedInstanceState?.getInt(super.POSITION_KEY, 0)  ?: 0

        with(intent.getStringExtra(REQUEST_TYPE)) {
            title = this
            movieListProvider = dispatcher.getValue(this)
            movieListProvider.let {
                async(UI) { super.createGUI(bg { loadPages(1, curPage)}.await(), curPos) }
            }
        }
    }
}
