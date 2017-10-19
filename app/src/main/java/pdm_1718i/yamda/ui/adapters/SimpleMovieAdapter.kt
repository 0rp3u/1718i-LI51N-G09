package pdm_1718i.yamda.ui.adapters

import android.app.Activity
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import pdm_1718i.yamda.model.Movie

/**
 * Created by orpheu on 10/19/17.
 */
class SimplesMovieAdapter(private val context : Activity, private val searchedMovies: List<Movie>,
                          private val itemClick: (Movie) -> Unit) : ArrayAdapter<Movie>(context, 1, searchedMovies){



}
