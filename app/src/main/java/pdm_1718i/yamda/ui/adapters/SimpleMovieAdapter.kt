package pdm_1718i.yamda.ui.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.server.Options
import pdm_1718i.yamda.extensions.getDateFromCalendar
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App
import pdm_1718i.yamda.ui.holders.SimpleMovieHolder

class SimpleMovieAdapter(private val context : Activity, private val searchedMovies: List<Movie>)
    : ArrayAdapter<Movie>(context,R.layout.item_search_result, searchedMovies){

    private val itemLayoutId = R.layout.item_search_result
    private val inflater = LayoutInflater.from(context)


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemView = convertView ?: inflater.inflate(itemLayoutId, parent, false)
        val movie : Movie = getItem(position)
        var movieViewHolder : SimpleMovieHolder
        if (convertView == null) {
            val image = itemView.findViewById<ImageView>(R.id.image)
            val title = itemView.findViewById<TextView>(R.id.title)
            val date = itemView.findViewById<TextView>(R.id.release_date)
            val score = itemView.findViewById<TextView>(R.id.score)
            movieViewHolder = SimpleMovieHolder(image,title, date, score)
            itemView.tag = movieViewHolder
        }else {
            movieViewHolder = itemView.tag as SimpleMovieHolder
        }

        movieViewHolder.title.text = movie.title
        movieViewHolder.date.text = getDateFromCalendar(movie.release_date)
        movieViewHolder.score.text= movie.vote_average.toString()

        if(movie.poster_path.isNotEmpty()){
            App.moviesProvider.image(movie.poster_path,  movieViewHolder.image, Options.poster_sizes["SMALL"]!!)
        }else{
            movieViewHolder.image.setImageResource(R.drawable.ic_movie_thumbnail)
        }

        return itemView
    }
    
    override fun getItem(position: Int): Movie {
        return searchedMovies[position]
    }

    fun getAllItems(): List<Movie>{
        return searchedMovies
    }
}
