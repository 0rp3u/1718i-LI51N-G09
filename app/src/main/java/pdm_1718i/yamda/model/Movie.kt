package pdm_1718i.yamda.model

import java.util.*

data class Movie(
        val poster_path : String,
        val release_date : Calendar?,
        val id: Int,
        val title : String,
        val backdrop_path : String,
        val vote_average: Double
)