package pdm_1718i.yamda.ui.activities

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.server.Options
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App

class MainActivity : AppCompatActivity() {
    private val DEFAULT_PAGINATION: Int = 1
    private val DEFAULT_ITEM_NUMBER: Int = 4

    private val POPULAR_ITEM_LIST: IntArray =
            intArrayOf(R.id.popular_image_1, R.id.popular_image_2, R.id.popular_image_3, R.id.popular_image_4)
    private val UPCOMING_ITEM_LIST: IntArray =
            intArrayOf(R.id.upcoming_image_1, R.id.upcoming_image_2, R.id.upcoming_image_3, R.id.upcoming_image_4)
    private val PLAYING_ITEM_LIST: IntArray =
            intArrayOf(R.id.theaters_image_1, R.id.theaters_image_2, R.id.theaters_image_3, R.id.theaters_image_4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        App.moviesProvider.popularMovies(DEFAULT_PAGINATION, {
            getImageAndSet(it, DEFAULT_ITEM_NUMBER, POPULAR_ITEM_LIST)
        })

        App.moviesProvider.upcomingMovies(DEFAULT_PAGINATION,{
            getImageAndSet(it, DEFAULT_ITEM_NUMBER, UPCOMING_ITEM_LIST)
        })

        App.moviesProvider.nowPlayingMovies(DEFAULT_PAGINATION,{
            getImageAndSet(it, DEFAULT_ITEM_NUMBER, PLAYING_ITEM_LIST)
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)

        val menuItem = menu.findItem(R.id.search_menu)
        val searchView = menuItem.actionView as SearchView
        searchView.setIconifiedByDefault(true)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(ComponentName(this, SearchResultActivity::class.java)))
        return true
    }


    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings ->
                true

            R.id.action_about ->{
                startActivity(Intent(applicationContext, AboutActivity::class.java))
                true
            }

            else ->
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                super.onOptionsItemSelected(item)
        }
    }


    fun onPopularMore(view: View){
        with(Intent(applicationContext, InTheatersActivity::class.java)){
            putExtra("requestType", "Popular")
            startActivity(this)
        }
    }

    fun onInTheatersMore(view: View){
        with(Intent(applicationContext, InTheatersActivity::class.java)){
            putExtra("requestType", "NowPlaying")
            startActivity(this)
        }
    }

    fun onUpcomingMore(view: View){
        with(Intent(applicationContext, InTheatersActivity::class.java)){
            putExtra("requestType", "Upcoming")
            startActivity(this)
        }
    }

   private fun getImageAndSet(it: List<Movie>, take: Int, image_views: IntArray){
        it.take(take).forEachIndexed { index, movie ->
            with(findViewById(image_views[index]) as ImageView){
                App.moviesProvider.image(movie.poster_path, this, Options.poster_sizes["SMALL"]!!)
                setOnClickListener({
                    startActivity(Intent(applicationContext, MovieDetailActivity::class.java).putExtra("movieId",movie.id ))
                })
            }
        }
    }


}
