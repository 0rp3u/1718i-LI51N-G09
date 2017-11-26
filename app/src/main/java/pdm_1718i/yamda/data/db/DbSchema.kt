package pdm_1718i.yamda.data.db

import android.provider.BaseColumns

object DbSchema {

    val DB_NAME = "movieInfo.db"
    val DB_VERSION = 1

    val COL_ID = BaseColumns._ID

    object Movie {
        val ADULT = MoviesContract.MOVIE.ADULT
        val BACKDROP = MoviesContract.MOVIE.BACKDROP
        val POSTER = MoviesContract.MOVIE.POSTER
        val TITLE = MoviesContract.MOVIE.TITLE
        val OVERVIEW = MoviesContract.MOVIE.OVERVIEW
        val ORIGINAL_TITLE = MoviesContract.MOVIE.ORIGINAL_TITLE

        val STRUCTURE = "$ADULT BOOLEAN , " +
        "$BACKDROP TEXT , " +
        "$POSTER TEXT , " +
        "$TITLE TEXT NOT NULL , " +
        "$OVERVIEW TEXT NOT NULL , " +
        "$ORIGINAL_TITLE TEXT NOT NULL"
    }

    object UpcoingMovies {
        val TBL_NAME = "upcoingMovies"
        val DDL_CREATE_TABLE =
                "CREATE TABLE $TBL_NAME ( " +
                        "$COL_ID INTEGER PRIMARY KEY, " +
                        Movie.STRUCTURE +
                        ")"

        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME
    }

    object TrendingMovies {
        val TBL_NAME = "trendingMovies"
        val DDL_CREATE_TABLE =
                "CREATE TABLE ${TBL_NAME} ( " +
                        "$COL_ID INTEGER PRIMARY KEY, " +
                        Movie.STRUCTURE +
                        ")"

        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME
    }
}