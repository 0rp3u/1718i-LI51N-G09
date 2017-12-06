package pdm_1718i.yamda.ui.holders

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.ListView
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.adapters.EndlessAdapter


class EndlessListView : ListView, AbsListView.OnScrollListener{

    constructor(context:Context, attrs:AttributeSet, defStyle:Int) : super(context, attrs, defStyle) {
        this.setOnScrollListener(this)
    }

    constructor(context:Context, attrs:AttributeSet) : super(context, attrs) {
        this.setOnScrollListener(this)
    }

    constructor(context:Context) : super(context) {
        this.setOnScrollListener(this)
    }

    private var footer: View? = null
    private var isLoading: Boolean = false
    private var listener: EndlessListener? = null
    private var adapter: EndlessAdapter? = null

    fun setListener(listener: EndlessListener)
    {
        this.listener = listener
    }

    override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {

        if (getAdapter()==null) return
        if(getAdapter().count==0) return
        val l : Int = visibleItemCount + firstVisibleItem
        if(l >= totalItemCount && !isLoading){
            //Add new data
            this.addFooterView(footer)
            isLoading = true
            listener?.loadData()
        }
    }

    override fun onScrollStateChanged(view: AbsListView?, loadState: Int) {}

    fun setLoadingView(resId: Int){
        var inflater: LayoutInflater = super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        footer = inflater.inflate(resId,null)
        this.addFooterView(footer)
    }

    fun setAdapter(adapter:EndlessAdapter)
    {
        super.setAdapter(adapter)
        this.adapter = adapter
        this.removeFooterView(footer)
    }

    fun addNewData(movies : List<Movie> )
    {
        this.removeFooterView(footer)
        adapter?.addAll(movies)
        adapter?.notifyDataSetChanged()
        isLoading = false
    }

    fun getListener() : EndlessListener?
    {
        return listener
    }

    interface EndlessListener {
        fun loadData()
    }
}