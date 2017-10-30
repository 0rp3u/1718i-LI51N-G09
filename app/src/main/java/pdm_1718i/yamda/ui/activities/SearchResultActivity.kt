package pdm_1718i.yamda.ui.activities

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import pdm_1718i.yamda.R
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App
import pdm_1718i.yamda.ui.adapters.SimplesMovieAdapter

class SearchResultActivity: BaseActivity() {

    private val listView: ListView by lazy { findViewById(R.id.list) as ListView }
    private val emptyView: TextView by lazy { findViewById(R.id.emptyElement) as TextView }
    private val LIST_KEY = "LIST_MOVIE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        handleIntent(intent)
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        with(listView.adapter as SimplesMovieAdapter){
//            outState.putParcelableArrayList(LIST_KEY, ArrayList(this.getAllItems()))
//        }
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        createGUI(savedInstanceState.getParcelableArrayList(LIST_KEY))
//    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            title = "Results Found for: $query"
            App.moviesProvider.searchMovies(query, 1, { createGUI(it) })
        }
    }

    private fun createGUI(movies: List<Movie>) {
        if (movies.isNotEmpty()) {
            listView.adapter = SimplesMovieAdapter(this, movies)
            listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val movieId = (listView.adapter.getItem(position) as Movie).id
                val intent: Intent = Intent(applicationContext, MovieDetailActivity::class.java).putExtra("movieId", movieId)
                startActivity(intent)
            }
        } else{
            emptyView.visibility = View.VISIBLE
            listView.emptyView = emptyView
        }
    }
}