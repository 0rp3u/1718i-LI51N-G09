package pdm_1718i.yamda.ui.activities

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import pdm_1718i.yamda.R


open class BaseActivity(val withMenu: Boolean = true) : AppCompatActivity() {

    lateinit var searchView: SearchView
    private var current_query: String? = null
    private val SEARCH_QUERY_KEY = "search_query"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState != null) {
            current_query = savedInstanceState.getString(SEARCH_QUERY_KEY);
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        current_query = try {searchView.query?.toString()} catch (t: UninitializedPropertyAccessException) {null}
        outState?.putString(SEARCH_QUERY_KEY, current_query)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.action_menu, menu)
        val menuItem = menu.findItem(R.id.search_menu)
        searchView = menuItem.actionView as SearchView
        searchView.setIconifiedByDefault(true)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(ComponentName(this, SearchResultActivity::class.java)))

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        return if(withMenu) super.onPrepareOptionsMenu(menu) else withMenu
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}