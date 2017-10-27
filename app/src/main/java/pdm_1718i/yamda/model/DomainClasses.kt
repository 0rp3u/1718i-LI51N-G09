package pdm_1718i.yamda.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by orpheu on 10/18/17.
 */

data class Movie(
        val poster_path : String,
        val release_date : String,
        val id: Int,
        val title : String,
        val backdrop_path : String,
        val vote_average: Double
)


data class DetailedMovie(
        val poster_path: String?,
        val release_date: String,
        val id: Int,
        val title: String,
        val vote_average: Float,
        val budget: Int,
        val genres: List<Genre>,
        val overview: String?
)

data class Genre(
        val name: String
)