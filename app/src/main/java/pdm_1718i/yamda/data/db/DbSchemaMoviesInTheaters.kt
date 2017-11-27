package pdm_1718i.yamda.data.db

import android.provider.BaseColumns

/**
 * Created by Edgar on 26/11/2017.
 */
object DbSchemaMoviesInTheaters {

    val COL_ID = BaseColumns._ID

    object Movie {
        val VOTE_AVERAGE = MoviesInTheatersContract.Movies.FIELDS.getValue("VOTE_AVERAGE")
        val BACKDROP = MoviesInTheatersContract.Movies.FIELDS.getValue("BACKDROP_PATH")
        val POSTER = MoviesInTheatersContract.Movies.FIELDS.getValue("POSTER_PATH")
        val TITLE = MoviesInTheatersContract.Movies.FIELDS.getValue("TITLE")
        val RELEASE_DATE = MoviesInTheatersContract.Movies.FIELDS.getValue("RELEASE_DATE")

        val STRUCTURE = "$RELEASE_DATE DATE , " +
                "$BACKDROP TEXT , " +
                "$POSTER TEXT , " +
                "$TITLE TEXT NOT NULL , " +
                "$VOTE_AVERAGE DOUBLE "
    }

    object inTheatersMovies {
        val TBL_NAME = "intheatersMovies"
        val DDL_CREATE_TABLE =
                "CREATE TABLE ${TBL_NAME} ( " +
                        "$COL_ID INTEGER PRIMARY KEY, " +
                        Movie.STRUCTURE +
                        ")"

        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME
    }
}