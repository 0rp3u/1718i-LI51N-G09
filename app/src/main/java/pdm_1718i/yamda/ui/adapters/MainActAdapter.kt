package pdm_1718i.yamda.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.server.Options
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App
import pdm_1718i.yamda.ui.holders.RecyclerViewHolder


class MainActAdapter(private var movies : List<Movie>) : RecyclerView.Adapter<RecyclerViewHolder>(){

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val movie = movies[position]
        holder.bindMovie(movie)
        if(movie.poster_path.isNotEmpty())
            App.moviesProvider.image(movie.poster_path, holder.imageView, Options.poster_sizes["SMALL"]!!)
        else holder.imageView.setImageResource(R.drawable.ic_movie_thumbnail)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.main_row_layout, parent, false)
        return RecyclerViewHolder(v)
    }

    fun setData(items : List<Movie>){
        this.movies = items
        this.notifyDataSetChanged()
    }
}