package pdm_1718i.yamda.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import pdm_1718i.yamda.R
import pdm_1718i.yamda.extensions.runIf
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App
import pdm_1718i.yamda.ui.adapters.SimplesMovieAdapter

class SearchResultActivity: BaseListActivity(listView_id = R.id.list, emptyElement_id = R.id.emptyElement) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        runIf({App.isNetworkAvailable}){
            handleIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val query = intent.getStringExtra("query")
        title = "Results for: $query"
        App.moviesProvider.searchMovies(query, 1, { createGUI(it) })
    }

    private fun createGUI(movies: List<Movie>) {
        if (movies.isNotEmpty()) {
            listView.adapter = SimplesMovieAdapter(this, movies)
            listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val movieId = (listView.adapter.getItem(position) as Movie).id
                with(Intent(applicationContext, MovieDetailActivity::class.java).putExtra("movieId", movieId)){
                    startActivity(this)
                }
            }
        } else{
            emptyView.visibility = View.VISIBLE
            listView.emptyView = emptyView
        }
    }
}