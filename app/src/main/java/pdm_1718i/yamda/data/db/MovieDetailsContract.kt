package pdm_1718i.yamda.data.db

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns

/**
 * Created by Edgar on 26/11/2017.
 */

object MoviesDetailsContract : ContractInterface {

    override val AUTHORITY: String = "pt.android.movies.provider.MovieProvider"
    override val MEDIA_BASE_SUBTYPE: String = "/vnd.movies."
    override val CONTENT_URI: Uri = Uri.parse("content://" + AUTHORITY + "/")

    object Movies : BaseColumns {
        val RESOURCE = "moviedetails"

        val CONTENT_URI2 = Uri.withAppendedPath(
                CONTENT_URI,
                RESOURCE
        )

        val CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + MEDIA_BASE_SUBTYPE + RESOURCE

        val FIELDS = mapOf(
                "_ID" to "_ID",
                "ID" to "id",
                "ORIGINAL_TITLE" to "original_title",
                "TITLE" to "title",
                "RELEASE_DATE" to "release_date",
                "VOTE_AVERAGE" to "vote_average",
                "POSTER_PATH" to "poster_path",
                "BACKDROP_PATH" to "backdrop_path",
                "OVERVIEW" to "overview",
                "ADULT" to "adult"
        )

        val PROJECT_ALL = FIELDS.values.toTypedArray()
        val DEFAULT_SORT_ORDER = String()
    }
}