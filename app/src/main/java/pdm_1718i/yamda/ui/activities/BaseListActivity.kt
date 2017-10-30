package pdm_1718i.yamda.ui.activities

import android.os.Bundle
import android.widget.ListView
import android.widget.TextView

/**
 * Created by Red on 30/10/2017.
 */
open class BaseListActivity(actionBar: Boolean = true, listView_id: Int, emptyElement_id: Int) : BaseActivity(actionBar) {

    protected val listView: ListView by lazy { findViewById(listView_id) as ListView }
    protected val emptyView: TextView by lazy { findViewById(emptyElement_id) as TextView }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        listView.verticalScrollbarPosition = savedInstanceState.getInt("position", 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("position", listView.firstVisiblePosition)
    }
}