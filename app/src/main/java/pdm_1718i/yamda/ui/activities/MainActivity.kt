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
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App

class MainActivity : AppCompatActivity() {
    val DEFAULT_PAGINATION: Int = 1
    val DEFAULT_ITEM_NUMBER: Int = 4

    val POPULAR_ITEM_LIST: IntArray =
            intArrayOf(R.id.popular_image_1, R.id.popular_image_2, R.id.popular_image_3, R.id.popular_image_4)
    val UPCOMING_ITEM_LIST: IntArray =
            intArrayOf(R.id.upcoming_image_1, R.id.upcoming_image_2, R.id.upcoming_image_3, R.id.upcoming_image_4)
    val PLAYING_ITEM_LIST: IntArray =
            intArrayOf(R.id.theaters_image_1, R.id.theaters_image_2, R.id.theaters_image_3, R.id.theaters_image_4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        App.moviesProvider.getPopularMovies(DEFAULT_PAGINATION, {
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


    fun onPopularMore(view : View){
        val intent: Intent = Intent(applicationContext, PopularMoviesActivity::class.java)
        startActivity(intent)

    }

    fun onInTheatersMore(view : View){
        val intent: Intent = Intent(applicationContext, InTheatersActivity::class.java)
        startActivity(intent)

    }

    fun onUpcomingMore(view : View){
        val intent: Intent = Intent(applicationContext, UpcomingActivity::class.java)
        startActivity(intent)
    }

   private fun getImageAndSet(it: List<Movie>, take: Int, image_views: IntArray){
        it.take(take).forEachIndexed { index, movie ->
            App.moviesProvider.getImage(movie.poster_path,{
                val image_view = findViewById(image_views[index]) as ImageView
                image_view.setImageBitmap(it)
            })
        }
    }


}
