package pdm_1718i.yamda.ui.adapters

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import pdm_1718i.yamda.R
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.holders.SimpleMovieHolder
import java.util.ArrayList

/**
 * Created by orpheu on 10/19/17.
 */



class SimplesMovieAdapter(private val context : Activity, private val searchedMovies: List<Movie>
                          ) : ArrayAdapter<Movie>(context,R.layout.item_search_result, searchedMovies){

    val itemLayoutId = R.layout.item_search_result
    val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemView = convertView ?: inflater.inflate(itemLayoutId, parent, false)
        val movie : Movie = getItem(position)
        var movieViewHolder : SimpleMovieHolder
        if (convertView == null) {
            val image = itemView.findViewById<ImageView>(R.id.image)
            val title = itemView.findViewById<TextView>(R.id.title)
            val score = itemView.findViewById<TextView>(R.id.score)
            movieViewHolder = SimpleMovieHolder(image,title, score)
            itemView.tag = movieViewHolder
        }else {
            movieViewHolder = itemView.tag as SimpleMovieHolder
        }

        movieViewHolder.title.text = movie.title
        movieViewHolder.score.text= movie.vote_average.toString()
        movieViewHolder.image.setImageResource(R.drawable.ic_movie_thumbnail)


        return itemView
    }

    override fun getItem(position: Int): Movie {
        return searchedMovies[position]
    }
}
