package pdm_1718i.yamda.model

import java.util.*

/**
 * Created by Red on 31/10/2017.
 */
data class DetailedMovie(
        val poster_path: String?,
        val backdrop_path: String?,
        val release_date: Calendar?,
        val id: Int,
        val title: String,
        val vote_average: Float,
        val budget: Int,
        val genres: List<Genre>,
        val overview: String?
)