package pdm_1718i.yamda.data.db


import android.provider.BaseColumns

object DbSchema {
    val DB_NAME = "movieInfo.db"
    val DB_VERSION = 1

    val COL_ID = BaseColumns._ID

    object MovieDetails {
        val VOTE_AVERAGE =      MovieContract.MovieDetails.VOTE_AVERAGE
        val BACKDROP =          MovieContract.MovieDetails.BACKDROP_PATH
        val POSTER =            MovieContract.MovieDetails.POSTER_PATH
        val TITLE =             MovieContract.MovieDetails.TITLE
        val ORIGINAL_TITLE =    MovieContract.MovieDetails.ORIGINAL_TITLE
        val OVERVIEW =          MovieContract.MovieDetails.OVERVIEW
        val ADULT =             MovieContract.MovieDetails.ADULT
        val RELEASE_DATE =      MovieContract.MovieDetails.RELEASE_DATE
        val IS_FOLLOWING =      MovieContract.MovieDetails.IS_FOLLOWING

        val STRUCTURE = "$RELEASE_DATE DATE , " +
                "$BACKDROP TEXT , " +
                "$POSTER TEXT , " +
                "$TITLE TEXT NOT NULL , " +
                "$ORIGINAL_TITLE TEXT NOT NULL , " +
                "$OVERVIEW TEXT , " +
                "$ADULT BOOLEAN , " +
                "$VOTE_AVERAGE DOUBLE , " +
                "$IS_FOLLOWING BOOLEAN "

        val TBL_NAME = "MovieDetails"

        val DDL_CREATE_TABLE =
                "CREATE TABLE $TBL_NAME ( " +
                        "$COL_ID INTEGER PRIMARY KEY, " +
                        STRUCTURE +
                        ")"

        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME
    }

    object UpcomingMovies{
        val ID = MovieContract.UpcomingMovies._ID
        val TBL_NAME = "UpcomingMovies"
        val DDL_CREATE_TABLE =
                "CREATE TABLE $TBL_NAME ( " +
                        "$ID INTEGER PRIMARY KEY, " +
                        "PAGE INTEGER" +
                        ")"

        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME
    }

    object NowPlayingMovies{
        val ID = MovieContract.NowPlayingMovies._ID
        val TBL_NAME = "NowPlayingMovies"
        val DDL_CREATE_TABLE =
                "CREATE TABLE $TBL_NAME ( " +
                        "$ID INTEGER PRIMARY KEY, " +
                        "PAGE INTEGER" +
                        ")"
        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME
    }

    object MostPopularMovies{
        val ID = MovieContract.MostPopularMovies._ID
        val TBL_NAME = "MostPopularMovies"
        val DDL_CREATE_TABLE =
                "CREATE TABLE $TBL_NAME ( " +
                        "$ID INTEGER PRIMARY KEY, " +
                        "PAGE INTEGER" +
                        ")"

        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME
    }
}