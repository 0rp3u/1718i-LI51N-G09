package pdm_1718i.yamda.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import pdm_1718i.yamda.R
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App
import pdm_1718i.yamda.ui.adapters.SimplesMovieAdapter

class MovieListActivity : BaseActivity() {

    private val listView: ListView by lazy { findViewById(R.id.list) as ListView }
    private val emptyView : TextView by lazy { findViewById(R.id.emptyElement) as TextView}
    private var domain : List<Movie> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        val reqType  = intent.getStringExtra("requestType")
        title = reqType
        if(savedInstanceState == null)
            when (reqType) {
                "Now Playing" -> App.moviesProvider.nowPlayingMovies(1, { createGUI(it) })
                "Popular" -> App.moviesProvider.popularMovies(1, { createGUI(it) })
                "Upcoming" -> App.moviesProvider.upcomingMovies(1, { createGUI(it) })
                else -> Toast.makeText(App.instance, "Something Went KABOOM", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(!domain.isEmpty()) outState.putParcelableArray("domain", domain.toTypedArray())
        Log.d("restore", "saved state to: ${domain.size} objects")

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        domain = (savedInstanceState.getParcelableArray("domain") as Array<Movie>).toList()
        Log.d("restore", "restored state to: ${domain.size} objects")
        createGUI(domain)
    }


    private fun createGUI(movies : List< Movie>){
        if(!movies.isEmpty()) {
            if(domain.isEmpty()) domain = movies
            listView.adapter = SimplesMovieAdapter(this, movies)
            listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val movieId = (listView.adapter.getItem(position) as Movie).id
                startActivity(Intent(applicationContext, MovieDetailActivity::class.java).putExtra("movieId", movieId))
            }
        }else{
            emptyView.visibility = View.VISIBLE
            listView.emptyView = emptyView

        }
    }


}
