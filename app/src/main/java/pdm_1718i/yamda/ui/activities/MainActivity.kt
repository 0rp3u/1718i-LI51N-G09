package pdm_1718i.yamda.ui.activities

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.server.TMDBService

class MainActivity : AppCompatActivity() {

    val tmdbServide by lazy{ TMDBService()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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


}
