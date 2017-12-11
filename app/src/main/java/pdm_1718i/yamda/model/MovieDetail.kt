package pdm_1718i.yamda.model

import android.content.ContentValues
import pdm_1718i.yamda.data.db.MovieContract
import pdm_1718i.yamda.extensions.getDateFromCalendar
import java.util.*


data class MovieDetail(
        val poster_path: String?,
        val backdrop_path: String?,
        val release_date: Calendar?,
        val id: Int,
        val title: String,
        val vote_average: Float,
        val budget: Int,
        val genres: List<Genre>,
        val overview: String?,
        val isFollowing: Boolean = false
){
    fun toContentValues() = ContentValues().apply {
        put(MovieContract.MovieDetails._ID, id)
        put(MovieContract.MovieDetails.IS_FOLLOWING, isFollowing)
        put(MovieContract.MovieDetails.POSTER_PATH, poster_path)
        put(MovieContract.MovieDetails.BACKDROP_PATH, backdrop_path)
        put(MovieContract.MovieDetails.VOTE_AVERAGE, vote_average.toDouble())
        put(MovieContract.MovieDetails.TITLE, title)
        put(MovieContract.MovieDetails.RELEASE_DATE, getDateFromCalendar(release_date))
        put(MovieContract.MovieDetails.OVERVIEW, overview)
        put(MovieContract.MovieDetails.ORIGINAL_TITLE, title)
    }
}



