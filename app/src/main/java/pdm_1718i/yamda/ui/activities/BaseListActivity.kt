package pdm_1718i.yamda.ui.activities

import android.widget.TextView
import pdm_1718i.yamda.ui.holders.EndlessListView

open class BaseListActivity(actionBar: Boolean = true, listView_id: Int, emptyElement_id: Int) : BaseActivity(actionBar) {
    protected val listView: EndlessListView by lazy { findViewById(listView_id) as EndlessListView}
    protected val emptyView: TextView by lazy { findViewById(emptyElement_id) as TextView }

}