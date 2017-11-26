package pdm_1718i.yamda.data.db

import android.provider.BaseColumns

/**
 * Created by Edgar on 26/11/2017.
 */
object DbSchemaMoviesUpcoming {

    val COL_ID = BaseColumns._ID

    object Movie {
        val VOTE_AVERAGE = MoviesUpcomingContract.Movies.FIELDS.getValue("VOTE_AVERAGE")
        val BACKDROP = MoviesUpcomingContract.Movies.FIELDS.getValue("BACKDROP_PATH")
        val POSTER = MoviesUpcomingContract.Movies.FIELDS.getValue("POSTER_PATH")
        val TITLE = MoviesUpcomingContract.Movies.FIELDS.getValue("TITLE")
        val RELEASE_DATE = MoviesUpcomingContract.Movies.FIELDS.getValue("RELEASE_DATE")

        val STRUCTURE = "$RELEASE_DATE DATE , " +
                "$BACKDROP TEXT , " +
                "$POSTER TEXT , " +
                "$TITLE TEXT NOT NULL , " +
                "$VOTE_AVERAGE DOUBLE "
    }

    object UpcomingMovies {
        val TBL_NAME = "upcomingMovies"
        val DDL_CREATE_TABLE =
                "CREATE TABLE $TBL_NAME ( " +
                        "$COL_ID INTEGER PRIMARY KEY, " +
                        Movie.STRUCTURE +
                        ")"

        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME
    }
}