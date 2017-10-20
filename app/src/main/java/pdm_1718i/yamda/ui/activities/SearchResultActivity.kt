package pdm_1718i.yamda.ui.activities

import android.app.ListActivity
import android.os.Bundle
import pdm_1718i.yamda.R
import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App
import pdm_1718i.yamda.ui.adapters.SimplesMovieAdapter
import android.widget.AdapterView




class SearchResultActivity: ListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)

            doMySearch(query)
        }
    }


    private fun doMySearch(query :String){

        App.moviesController.movieSearch(query,1, {
            listView.adapter = SimplesMovieAdapter(this, it)
            listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val movieId = (listView.adapter.getItem(position) as Movie).id
                val intent: Intent = Intent(applicationContext, MovieDetailActivity::class.java).putExtra("movieId",movieId  )
                startActivity(intent)
            }
        })

    }
}