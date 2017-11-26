package pdm_1718i.yamda.data.db

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns

object MoviesContract : ContractInterface{

    override val AUTHORITY: String = "pt.android.movies.provider.MovieProvider"
    override val MEDIA_BASE_SUBTYPE: String = "/vnd.movies."

    object Movies : BaseColumns {
        val RESOURCE = "movies"

        val CONTENT_URI: Uri = Uri.parse("content://" + AUTHORITY + "/")

        val CONTENT_URI2 = Uri.withAppendedPath(
                MoviesContract.CONTENT_URI,
                RESOURCE
        )

        val CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val FIELDS = mapOf(
        "_ID" to "_ID",
                "ID" to "id",
                "TITLE" to "title",
                "RELEASE_DATE" to "release_date",
                "VOTE_AVERAGE" to "vote_average",
                "POSTER_PATH" to "poster_path",
                "BACKDROP_PATH" to "backdrop_path"
        )

        val PROJECT_ALL = FIELDS.values.toTypedArray()
        val DEFAULT_SORT_ORDER = String()
    }
}