package pdm_1718i.yamda.ui.activities

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import pdm_1718i.yamda.R

/**
 * Created by orpheu on 10/26/17.
 */

interface ToolbarManager {

    val toolbar: Toolbar

    var toolbarTitle: String
        get() = toolbar.title.toString()
        set(value) {
            toolbar.title = value
        }

    fun initToolbar() {
        toolbar.inflateMenu(R.menu.action_menu)

        val search = toolbar.menu.findItem(R.id.search_menu)
        val searchView = search.actionView as SearchView

        val searchManager = toolbar.context.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(ComponentName(toolbar.context, SearchResultActivity::class.java)))

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_settings -> {}
                R.id.action_about ->{
                    toolbar.context.startActivity(Intent(toolbar.context, AboutActivity::class.java))
                }
                //else -> App.instance.toast("Unknown option")
            }



            true
        }
    }

}