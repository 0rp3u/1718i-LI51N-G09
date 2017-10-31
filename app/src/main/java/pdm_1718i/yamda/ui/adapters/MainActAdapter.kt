package pdm_1718i.yamda.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.server.Options
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App
import pdm_1718i.yamda.ui.holders.RecyclerViewHolder

/**
 * Created by Edgar on 30/10/2017.
 */

class MainActAdapter(val movies : List<Movie>) : RecyclerView.Adapter<RecyclerViewHolder>(){

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val movie = movies[position]
        holder.bindMovie(movie)
        App.moviesProvider.image(movie.poster_path, holder.imageView, Options.poster_sizes["SMALL"]!!)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.main_row_layout, parent, false)
        return RecyclerViewHolder(v)
    }
}