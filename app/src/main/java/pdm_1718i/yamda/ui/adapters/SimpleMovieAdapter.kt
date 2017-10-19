package pdm_1718i.yamda.ui.adapters

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.TextView
import pdm_1718i.yamda.R
import pdm_1718i.yamda.model.Movie

/**
 * Created by orpheu on 10/19/17.
 */



class SimplesMovieAdapter(private val context : Activity, private val searchedMovies: List<Movie>,
                          private val itemClick: (Movie) -> Unit) : ArrayAdapter<Movie>(context,R.layout.item_search_result, searchedMovies){

    val itemLayoutId = R.layout.item_search_result
    val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemView =
                convertView ?:
                        inflater.inflate(itemLayoutId, parent, false)

        return itemView
    }






}
