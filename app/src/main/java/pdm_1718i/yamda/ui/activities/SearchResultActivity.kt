package pdm_1718i.yamda.ui.activities

import android.app.ListActivity
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.pdm_1718i.yamda.data.server.MovieSearchResult
import pdm_1718i.yamda.R
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App
import pdm_1718i.yamda.ui.adapters.SimplesMovieAdapter

class SearchResultActivity: AppCompatActivity() {

    private val listView: ListView by lazy { findViewById(R.id.list) as ListView }
    private val emptyView: TextView by lazy { findViewById(R.id.emptyElement) as TextView }

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
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            title = "Results Found for: $query"
            App.moviesProvider.searchMovies(query, 1, { createGUI(it) })
        }
    }


    private fun createGUI(movies: List<Movie>) {
        if (!movies.isEmpty()) {
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