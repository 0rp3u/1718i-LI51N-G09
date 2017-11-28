package pdm_1718i.yamda.data.db

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils


class MovieProvider: ContentProvider(){

    object DETAILS{
        val CURSOR = 1
        val ITEM = 2
    }

    object UPCOMING{
        val CURSOR = 3
        val ITEM = 4
    }

    object THEATERS{
        val CURSOR = 5
        val ITEM = 6
    }

    private val URI_MATCHER: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {

        /*****************************************************************************/
        URI_MATCHER.addURI(MovieContract.AUTHORITY,
                MovieContract.NowPlayingMovies.RESOURCE,
                THEATERS.CURSOR)

        URI_MATCHER.addURI(MovieContract.AUTHORITY,
                "${MovieContract.NowPlayingMovies.RESOURCE}/#",
                THEATERS.ITEM)

        /*****************************************************************************/
        URI_MATCHER.addURI(MovieContract.AUTHORITY,
                MovieContract.UpcomingMovies.RESOURCE,
                UPCOMING.CURSOR)

        URI_MATCHER.addURI(MovieContract.AUTHORITY,
                "${MovieContract.UpcomingMovies.RESOURCE}/#",
                UPCOMING.ITEM)

        /*****************************************************************************/
        URI_MATCHER.addURI(MovieContract.AUTHORITY,
                MovieContract.MovieDetails.RESOURCE,
                DETAILS.CURSOR)

        URI_MATCHER.addURI(MovieContract.AUTHORITY,
                "${MovieContract.MovieDetails.RESOURCE}/#",
                DETAILS.ITEM)

        /*****************************************************************************/
    }

    private lateinit var dbHelper: MoviesDbHelper


    override fun onCreate(): Boolean {
        dbHelper = MoviesDbHelper(context)
        return true
    }

    override fun getType(uri: Uri?): String {
        return when(URI_MATCHER.match(uri))
        {
            THEATERS.CURSOR -> MovieContract.NowPlayingMovies.CONTENT_TYPE
            THEATERS.ITEM ->  MovieContract.NowPlayingMovies.CONTENT_ITEM_TYPE

            UPCOMING.CURSOR -> MovieContract.UpcomingMovies.CONTENT_TYPE
            UPCOMING.ITEM -> MovieContract.UpcomingMovies.CONTENT_ITEM_TYPE

            DETAILS.CURSOR -> MovieContract.MovieDetails.CONTENT_TYPE
            DETAILS.ITEM -> MovieContract.MovieDetails.CONTENT_ITEM_TYPE

            else-> throw badUri(uri)
        }
    }

    private fun badUri(uri: Uri?) =
            IllegalArgumentException("unknown uri: $uri")

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {        val qbuilder = SQLiteQueryBuilder()
        var sortOrder = sortOrder
        when (URI_MATCHER.match(uri)) {
            UPCOMING.CURSOR -> {
                qbuilder.tables = DbSchema.UpcomingMovies.TBL_NAME
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = MovieContract.UpcomingMovies.DEFAULT_SORT_ORDER
                }
            }
            UPCOMING.ITEM -> {
                qbuilder.tables = DbSchema.UpcomingMovies.TBL_NAME
                qbuilder.appendWhere("${DbSchema.COL_ID} = ${uri!!.lastPathSegment}")
            }
            else -> throw badUri(uri)
        }

        val db = dbHelper.readableDatabase
        val cursor = qbuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder)

        cursor.setNotificationUri(context.contentResolver, uri)
        
        return cursor

    }

    override fun insert(p0: Uri?, p1: ContentValues?): Uri {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(p0: Uri?, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(p0: Uri?, p1: String?, p2: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}