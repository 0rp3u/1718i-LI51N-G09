package pdm_1718i.yamda.model

/**
 * Created by orpheu on 10/18/17.
 */

data class Movie(
        val poster_path : String,
        val adult: Boolean,
        val overview: String,
        val release_date : String,
        val genre_ids : HashSet<Int>,
        val id: Int,
        val original_title : String,
        val original_language : String,
        val title : String,
        val backdrop_path : String,
        val popularity : Double,
        val vote_count : Int,
        val vote_average: Number

)