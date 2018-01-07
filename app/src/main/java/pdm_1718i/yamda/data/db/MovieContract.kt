package pdm_1718i.yamda.data.db

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns

object MovieContract : ContractInterface {

    override val AUTHORITY: String = "pt.android.movies.provider.DatabaseContentProvider"
    override val MEDIA_BASE_SUBTYPE: String = "/vnd.movies."
    override val BASE_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/")

    object MovieListId {
        val DETAILS_ID = "DETAILS_ID"
        val PAGE = "PAGE"
    }

    object MovieDetails : BaseColumns {
        val RESOURCE = "MovieDetails"

        val CONTENT_URI = Uri.withAppendedPath(
                BASE_CONTENT_URI,
                RESOURCE
        )

        val CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val _ID = BaseColumns._ID
        val ORIGINAL_TITLE = "original_title"
        val TITLE = "title"
        val RELEASE_DATE = "release_date"
        val VOTE_AVERAGE = "vote_average"
        val POSTER_PATH = "poster_path"
        val BACKDROP_PATH = "backdrop_path"
        val OVERVIEW = "overview"
        val ADULT = "adult"
        val BUDGET = "budget"
        val GENRES = "genres"
        val IS_FOLLOWING = "isFollowing"
        val IMDB_ID = "imdbId"


        val PROJECT_ALL = arrayOf(
            _ID,
            ORIGINAL_TITLE,
            TITLE,
            RELEASE_DATE,
            VOTE_AVERAGE,
            POSTER_PATH,
            BACKDROP_PATH,
            OVERVIEW,
            ADULT,
            BUDGET,
            GENRES,
            IS_FOLLOWING,
            IMDB_ID
        )
        val DEFAULT_SORT_ORDER = String()
    }

    object NowPlayingIds : BaseColumns{
        val RESOURCE = "NowPlayingIds"

        val CONTENT_URI = Uri.withAppendedPath(
                BASE_CONTENT_URI,
                RESOURCE
        )

        val CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val DETAILS_ID = MovieListId.DETAILS_ID
        val PAGE =  MovieListId.PAGE

        val PROJECT_ALL = arrayOf(DETAILS_ID, PAGE)
        val DEFAULT_SORT_ORDER = String()
    }

    object UpcomingIds: BaseColumns{
        val RESOURCE = "UpcomingIds"

        val CONTENT_URI = Uri.withAppendedPath(
                BASE_CONTENT_URI,
                RESOURCE
        )

        val CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val DETAILS_ID = MovieListId.DETAILS_ID
        val PAGE =  MovieListId.PAGE

        val PROJECT_ALL = arrayOf(DETAILS_ID, PAGE)
        val DEFAULT_SORT_ORDER = String()
    }

    object MostPopularIds : BaseColumns{
        val RESOURCE = "MostPopularIds"

        val CONTENT_URI = Uri.withAppendedPath(
                BASE_CONTENT_URI,
                RESOURCE
        )

        val CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE


        val DETAILS_ID = MovieListId.DETAILS_ID
        val PAGE =  MovieListId.PAGE

        val PROJECT_ALL = arrayOf(DETAILS_ID, PAGE)
        val DEFAULT_SORT_ORDER = String()
    }

    object NowPlayingMovies : BaseColumns{
        val RESOURCE = "NowPlayingMovies"

        val CONTENT_URI = Uri.withAppendedPath(
                BASE_CONTENT_URI,
                RESOURCE
        )

        val CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE


        val PROJECT_ALL = arrayOf(NowPlayingIds.PAGE).plus(MovieDetails.PROJECT_ALL)
        val DEFAULT_SORT_ORDER = String()
    }

    object UpcomingMovies : BaseColumns{
        val RESOURCE = "UpcomingMovies"

        val CONTENT_URI = Uri.withAppendedPath(
                BASE_CONTENT_URI,
                RESOURCE
        )

        val CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val PROJECT_ALL = arrayOf(UpcomingIds.PAGE).plus(MovieDetails.PROJECT_ALL)
        val DEFAULT_SORT_ORDER = String()
    }

    object MostPopularMovies : BaseColumns{
        val RESOURCE = "MostPopularMovies"

        val CONTENT_URI = Uri.withAppendedPath(
                BASE_CONTENT_URI,
                RESOURCE
        )

        val CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val PROJECT_ALL = arrayOf(MostPopularIds.PAGE).plus(MovieDetails.PROJECT_ALL)
        val DEFAULT_SORT_ORDER = String()
    }

}