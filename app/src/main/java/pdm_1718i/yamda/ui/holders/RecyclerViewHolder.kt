package pdm_1718i.yamda.ui.holders

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import pdm_1718i.yamda.R
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.activities.MovieDetailActivity

/**
 * Created by Edgar on 31/10/2017.
 */
class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

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