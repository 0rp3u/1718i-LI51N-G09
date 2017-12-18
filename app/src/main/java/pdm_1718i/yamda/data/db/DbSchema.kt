package pdm_1718i.yamda.data.db

object DbSchema {
    val DB_NAME = "moviedata.db"
    val DB_VERSION = 1


    object MovieDetails {

        val COL_ID =            MovieContract.MovieDetails._ID
        val VOTE_AVERAGE =      MovieContract.MovieDetails.VOTE_AVERAGE
        val BACKDROP_PATH =     MovieContract.MovieDetails.BACKDROP_PATH
        val POSTER_PATH =       MovieContract.MovieDetails.POSTER_PATH
        val TITLE =             MovieContract.MovieDetails.TITLE
        val ORIGINAL_TITLE =    MovieContract.MovieDetails.ORIGINAL_TITLE
        val OVERVIEW =          MovieContract.MovieDetails.OVERVIEW
        val ADULT =             MovieContract.MovieDetails.ADULT
        val RELEASE_DATE =      MovieContract.MovieDetails.RELEASE_DATE
        val BUDGET =            MovieContract.MovieDetails.BUDGET
        val GENRES =            MovieContract.MovieDetails.GENRES
        val IS_FOLLOWING =      MovieContract.MovieDetails.IS_FOLLOWING

        val STRUCTURE = "$RELEASE_DATE TEXT , " +
                "$BACKDROP_PATH TEXT , " +
                "$POSTER_PATH TEXT , " +
                "$TITLE TEXT NOT NULL , " +
                "$ORIGINAL_TITLE TEXT, " +
                "$OVERVIEW TEXT , " +
                "$GENRES TEXT , " +
                "$ADULT BOOLEAN , " +
                "$BUDGET INTEGER , " +
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

    object UpcomingIds {
        val TBL_NAME = "UpcomingIds"

        val DETAILS_ID = MovieContract.UpcomingIds.DETAILS_ID
        val PAGE = MovieContract.UpcomingIds.PAGE

        val DDL_CREATE_TABLE =
                "CREATE TABLE $TBL_NAME ( " +
                        "$DETAILS_ID INTEGER PRIMARY KEY," +
                        "$PAGE INTEGER" +
                        ")"

        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME
    }

    object NowPlayingIds {
        val TBL_NAME = "NowPlayingIds"

        val DETAILS_ID = MovieContract.NowPlayingIds.DETAILS_ID
        val PAGE = MovieContract.NowPlayingIds.PAGE

        val DDL_CREATE_TABLE =
                "CREATE TABLE $TBL_NAME ( " +
                        "$DETAILS_ID INTEGER PRIMARY KEY," +
                        "$PAGE INTEGER" +
                        ")"
        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME
    }

    object MostPopularIds {
        val TBL_NAME = "MostPopularIds"

        val DETAILS_ID = MovieContract.MostPopularIds.DETAILS_ID
        val PAGE = MovieContract.MostPopularIds.PAGE

        val DDL_CREATE_TABLE =
                "CREATE TABLE $TBL_NAME ( " +
                        "$DETAILS_ID INTEGER PRIMARY KEY," +
                        "$PAGE INTEGER" +
                        ")"

        val DDL_DROP_TABLE = "DROP TABLE IF EXISTS " + TBL_NAME

    }

    object MostPopularMovies{
        val VIEW_NAME = "MostPopularMovies"

        val DDL_CREATE_VIEW =
                "CREATE VIEW $VIEW_NAME AS " +
                    "SELECT * " +
                    "FROM ${MostPopularIds.TBL_NAME}, ${MovieDetails.TBL_NAME} "+
                    "WHERE ${MostPopularIds.TBL_NAME}.${MostPopularIds.DETAILS_ID} = ${MovieDetails.TBL_NAME}.${MovieDetails.COL_ID}"

        val DDL_DROP_VIEW = "DROP VIEW IF EXISTS " + VIEW_NAME
    }

    object NowPlayingMovies{
        val VIEW_NAME = "NowPlayingMovies"

        val DDL_CREATE_VIEW =
                "CREATE VIEW $VIEW_NAME AS " +
                        "SELECT * " +
                        "FROM ${NowPlayingIds.TBL_NAME}, ${MovieDetails.TBL_NAME} "+
                        "WHERE ${NowPlayingIds.TBL_NAME}.${NowPlayingIds.DETAILS_ID} = ${MovieDetails.TBL_NAME}.${MovieDetails.COL_ID}"


        val DDL_DROP_VIEW = "DROP VIEW IF EXISTS " + VIEW_NAME
    }

    object UpcomingMovies{
        val VIEW_NAME = "UpcomingMovies"

        val DDL_CREATE_VIEW =
                "CREATE VIEW $VIEW_NAME AS " +
                        "SELECT * " +
                        "FROM ${UpcomingIds.TBL_NAME}, ${MovieDetails.TBL_NAME} "+
                        "WHERE ${UpcomingIds.TBL_NAME}.${UpcomingIds.DETAILS_ID} = ${MovieDetails.TBL_NAME}.${MovieDetails.COL_ID}"

        val DDL_DROP_VIEW = "DROP VIEW IF EXISTS " + VIEW_NAME
    }

}