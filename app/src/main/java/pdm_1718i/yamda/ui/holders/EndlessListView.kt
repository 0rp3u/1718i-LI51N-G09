package pdm_1718i.yamda.ui.holders

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.ListView
import android.widget.TextView
import pdm_1718i.yamda.R
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.adapters.EndlessAdapter
import pdm_1718i.yamda.ui.adapters.EndlessListener


class EndlessListView(context: Context, attrs: AttributeSet) : ListView(context, attrs), AbsListView.OnScrollListener{

    private val inflater: LayoutInflater = super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    lateinit var footer: View
    var isLoading: Boolean = false
    private lateinit var listenerEndless: EndlessListener
    lateinit var adapterEndless: EndlessAdapter
    var full: Boolean = false

    fun setListener(listener: EndlessListener) {
        this.listenerEndless = listener
    }

    override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        if (::adapterEndless.isInitialized.not() || adapterEndless.count==0 )
            return

        if (::footer.isInitialized.not())
            setFooterView(R.layout.loading_layout)

        val l : Int = visibleItemCount + firstVisibleItem
        if(!full && l >= totalItemCount && !isLoading){
            isLoading = true
            //Add new data
            this.addFooterView(footer)
            listenerEndless.loadData()
        }
    }

    //Necessário fazer override AbsListView mas não necessário para o trabalho
    override fun onScrollStateChanged(view: AbsListView?, loadState: Int) {}


    fun setFooterView(layout: Int) {
        if (::footer.isInitialized)
        {
            this.removeFooterView(footer)
        }
        footer = inflater.inflate(layout, null) //Null to add and remove as we need it => Não fica associado a nennhum ViewGroup
        this.addFooterView(footer)

    }

    fun setFooterText(text : String){
        this.removeFooterView(footer)
        footer = inflater.inflate(R.layout.footer_text_layout,null) //Null to add and remove as we need it => Não fica associado a nennhum ViewGroup
        footer.findViewById<TextView>(R.id.footer_text)?.text = text
        this.addFooterView( footer)

    }

    fun setAdapter(adapter:EndlessAdapter)
    {
        super.setAdapter(adapter)
        this.adapterEndless = adapter
        this.removeFooterView(footer)
    }

    fun addNewData(movies : List<Movie> )
    {
        this.removeFooterView(footer)
        adapterEndless.addAll(movies)
        adapterEndless.notifyDataSetChanged()
        isLoading = false
    }

    fun getListener(): EndlessListener{
        return listenerEndless
    }

    fun setFull() {
        full = true
        setFooterText("NO MORE ITEMS TO SHOW")
    }

    fun setProblem(txt : String) {
        setFooterText(txt)
        footer.setOnClickListener {
            isLoading = false
            setFooterView(R.layout.loading_layout)
            listenerEndless.loadData()
        }
    }

    init {
        this.setOnScrollListener(this)
    }
}