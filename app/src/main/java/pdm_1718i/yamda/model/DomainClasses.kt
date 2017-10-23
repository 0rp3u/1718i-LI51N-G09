package pdm_1718i.yamda.model

/**
 * Created by orpheu on 10/18/17.
 */

data class Movie(
        val poster_path : String,
        val release_date : String,
        val id: Int,
        val title : String,
        val backdrop_path : String,
        val vote_average: Number
)