package pdm_1718i.yamda.data.db

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns

object MovieContract : ContractInterface {

    override val AUTHORITY: String = "pt.android.movies.provider.MovieProvider"
    override val MEDIA_BASE_SUBTYPE: String = "/vnd.movies."
    override val BASE_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/")

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

        val _ID = "_ID"
        val ORIGINAL_TITLE = "original_title"
        val TITLE = "title"
        val RELEASE_DATE = "release_date"
        val VOTE_AVERAGE = "vote_average"
        val POSTER_PATH = "poster_path"
        val BACKDROP_PATH = "backdrop_path"
        val OVERVIEW = "overview"
        val ADULT = "adult"
        val IS_FOLLOWING = "isFollowing"

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
            IS_FOLLOWING
        )
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

        val _ID = "_ID"

        val PROJECT_ALL = arrayOf(_ID)
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

        val _ID = "_ID"

        val PROJECT_ALL = arrayOf(_ID)
        val DEFAULT_SORT_ORDER = String()
    }
}