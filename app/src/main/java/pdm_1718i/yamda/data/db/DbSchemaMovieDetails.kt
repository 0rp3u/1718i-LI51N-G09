package pdm_1718i.yamda.data.db

import android.provider.BaseColumns

/**
 * Created by Edgar on 26/11/2017.
 */
object DbSchemaMovieDetails {

    val COL_ID = BaseColumns._ID

    object Details {
        val VOTE_AVERAGE = MoviesDetailsContract.Movies.FIELDS.getValue("VOTE_AVERAGE")
        val BACKDROP = MoviesDetailsContract.Movies.FIELDS.getValue("BACKDROP_PATH")
        val POSTER = MoviesDetailsContract.Movies.FIELDS.getValue("POSTER_PATH")
        val TITLE = MoviesDetailsContract.Movies.FIELDS.getValue("TITLE")
        val ORIGINAL_TITLE = MoviesDetailsContract.Movies.FIELDS.getValue("ORIGINAL_TITLE")
        val OVERVIEW = MoviesDetailsContract.Movies.FIELDS.getValue("OVERVIEW")
        val ADULT = MoviesDetailsContract.Movies.FIELDS.getValue("ADULT")
        val RELEASE_DATE = MoviesDetailsContract.Movies.FIELDS.getValue("RELEASE_DATE")


        val STRUCTURE = "$RELEASE_DATE DATE , " +
                "$BACKDROP TEXT , " +
                "$POSTER TEXT , " +
                "$TITLE TEXT NOT NULL , " +
                "$ORIGINAL_TITLE TEXT NOT NULL , " +
                "$OVERVIEW TEXT , " +
                "$ADULT BOOLEAN , " +
                "$VOTE_AVERAGE DOUBLE "
    }

    object MovieDetails {
        val TBL_NAME = "MovieDetails"
        val DDL_CREATE_TABLE =
                "CREATE TABLE ${TBL_NAME} ( " +
                        "$COL_ID INTEGER PRIMARY KEY, " +
                        Details.STRUCTURE +
                        ")"

        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME
    }
}