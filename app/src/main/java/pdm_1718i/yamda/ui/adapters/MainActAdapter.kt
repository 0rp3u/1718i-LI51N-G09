package pdm_1718i.yamda.ui.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.server.Options
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App
import pdm_1718i.yamda.ui.activities.MovieDetailActivity

/**
 * Created by Edgar on 30/10/2017.
 */

class MainActAdapter(val movies : List<Movie>) : RecyclerView.Adapter<MainActAdapter.ViewHolder>(){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bindMovie(movie)
        App.moviesProvider.image(movie.poster_path, holder.imageView, Options.poster_sizes["SMALL"]!!)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.main_row_layout, parent, false)
        return ViewHolder(v)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private lateinit var movie: Movie

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View) {
            val context = itemView.context
            val showMovieIntent = Intent(context, MovieDetailActivity::class.java)
            showMovieIntent.putExtra("movieId", movie.id)
            context.startActivity(showMovieIntent)
        }

        fun bindMovie(movie: Movie) {
            this.movie = movie
        }

        val imageView = itemView.findViewById<ImageView>(R.id.image_view_item)
    }
}