package pdm_1718i.yamda.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import pdm_1718i.yamda.R
import pdm_1718i.yamda.ui.App
import pdm_1718i.yamda.ui.App.Companion.isNetworkAvailable


open class BaseActivity(val withMenu: Boolean = true) : AppCompatActivity() {

    var searchQuery : String? = null
    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(withMenu){
            val query = try{searchView.query}catch (t: UninitializedPropertyAccessException){null}
            if(!query.isNullOrEmpty()) {
                outState.putString("query", query.toString())
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if(withMenu){
            searchQuery = savedInstanceState.getString("query")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        val menuItem = menu.findItem(R.id.search_menu)
        searchView = menuItem.actionView as SearchView
        searchView.setIconifiedByDefault(true)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!TextUtils.isEmpty(query)) {
                    with(Intent(this@BaseActivity, SearchResultActivity::class.java)) {
                        putExtra("query", query)
                        startActivity(this)
                    }
                }
                return true
            }
            //SearchView should perform the default action of showing any suggestions if available
            override fun onQueryTextChange(newText: String?): Boolean = false
        })

        if (!TextUtils.isEmpty(searchQuery)) {
            searchView.isIconified = false
            menuItem.expandActionView()
            searchView.setQuery(searchQuery, false)
            searchView.requestFocus()
        }


        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        return if (withMenu) super.onPrepareOptionsMenu(menu) else withMenu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings ->
                true
            R.id.action_about -> {
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