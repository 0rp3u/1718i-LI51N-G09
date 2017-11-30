package pdm_1718i.yamda.data.db


import android.provider.BaseColumns

object DbSchema {
    val DB_NAME = "movieInfo.db"
    val DB_VERSION = 1

    val COL_ID = BaseColumns._ID

    object MovieDetails {
        val VOTE_AVERAGE =      MovieContract.MovieDetails.FIELDS["VOTE_AVERAGE"]
        val BACKDROP =          MovieContract.MovieDetails.FIELDS["BACKDROP_PATH"]
        val POSTER =            MovieContract.MovieDetails.FIELDS["POSTER_PATH"]
        val TITLE =             MovieContract.MovieDetails.FIELDS["TITLE"]
        val ORIGINAL_TITLE =    MovieContract.MovieDetails.FIELDS["ORIGINAL_TITLE"]
        val OVERVIEW =          MovieContract.MovieDetails.FIELDS["OVERVIEW"]
        val ADULT =             MovieContract.MovieDetails.FIELDS["ADULT"]
        val RELEASE_DATE =      MovieContract.MovieDetails.FIELDS["RELEASE_DATE"]
        val IS_FOLLOWING =      MovieContract.MovieDetails.FIELDS["IS_FOLLOWING"]

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
        val ID = MovieContract.UpcomingMovies.FIELDS["_ID"]
        val TBL_NAME = "UpcomingMovies"
        val DDL_CREATE_TABLE =
                "CREATE TABLE $TBL_NAME ( " +
                        "$ID INTEGER PRIMARY KEY, " +
                        ")"

        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME
    }

    object NowPlayingMovies{
        val ID = MovieContract.NowPlayingMovies.FIELDS["_ID"]
        val TBL_NAME = "NowPlayingMovies"
        val DDL_CREATE_TABLE =
                "CREATE TABLE $TBL_NAME ( " +
                        "$ID INTEGER PRIMARY KEY, " +
                        ")"

        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME
    }
}