package pdm_1718i.yamda.data.db

import android.content.*
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.provider.BaseColumns

class MovieProvider: ContentProvider(){

    object DETAILS{
        val LIST = 1
        val ITEM = 2
    }

    object UPCOMING_IDS{
        val LIST = 3
        val ITEM = 4
    }

    object THEATERS_IDS{
        val LIST = 5
        val ITEM = 6
    }

    object POPULAR_IDS{
        val LIST = 7
        val ITEM = 8
    }

    object UPCOMING{
        val LIST = 9
        val ITEM = 10
    }

    object THEATERS{
        val LIST = 11
        val ITEM = 12
    }

    object POPULAR{
        val LIST = 13
        val ITEM = 14
    }

    private val uriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {

        /*****************************************************************************/
        uriMatcher.addURI(MovieContract.AUTHORITY,
                MovieContract.NowPlayingIds.RESOURCE,
                THEATERS_IDS.LIST)

        uriMatcher.addURI(MovieContract.AUTHORITY,
                "${MovieContract.NowPlayingIds.RESOURCE}/#",
                THEATERS_IDS.ITEM)

        /*****************************************************************************/
        uriMatcher.addURI(MovieContract.AUTHORITY,
                MovieContract.UpcomingIds.RESOURCE,
                UPCOMING_IDS.LIST)

        uriMatcher.addURI(MovieContract.AUTHORITY,
                "${MovieContract.UpcomingIds.RESOURCE}/#",
                UPCOMING_IDS.ITEM)

        /*****************************************************************************/
        uriMatcher.addURI(MovieContract.AUTHORITY,
                MovieContract.MostPopularIds.RESOURCE,
                POPULAR_IDS.LIST)

        uriMatcher.addURI(MovieContract.AUTHORITY,
                "${MovieContract.MostPopularIds.RESOURCE}/#",
                POPULAR_IDS.ITEM)


        /*****************************************************************************/
        uriMatcher.addURI(MovieContract.AUTHORITY,
                MovieContract.NowPlayingMovies.RESOURCE,
                THEATERS.LIST)

        uriMatcher.addURI(MovieContract.AUTHORITY,
                "${MovieContract.NowPlayingMovies.RESOURCE}/#",
                THEATERS.ITEM)

        /*****************************************************************************/
        uriMatcher.addURI(MovieContract.AUTHORITY,
                MovieContract.UpcomingMovies.RESOURCE,
                UPCOMING.LIST)

        uriMatcher.addURI(MovieContract.AUTHORITY,
                "${MovieContract.UpcomingMovies.RESOURCE}/#",
                UPCOMING.ITEM)

        /*****************************************************************************/
        uriMatcher.addURI(MovieContract.AUTHORITY,
                MovieContract.MostPopularMovies.RESOURCE,
                POPULAR.LIST)

        uriMatcher.addURI(MovieContract.AUTHORITY,
                "${MovieContract.MostPopularMovies.RESOURCE}/#",
                POPULAR.ITEM)

        /*****************************************************************************/
        uriMatcher.addURI(MovieContract.AUTHORITY,
                MovieContract.MovieDetails.RESOURCE,
                DETAILS.LIST)

        uriMatcher.addURI(MovieContract.AUTHORITY,
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
        return when(uriMatcher.match(uri))
        {
            DETAILS.LIST  ->        MovieContract.MovieDetails.CONTENT_TYPE
            DETAILS.ITEM  ->        MovieContract.MovieDetails.CONTENT_ITEM_TYPE

            THEATERS.LIST ->        MovieContract.NowPlayingMovies.CONTENT_TYPE
            THEATERS.ITEM ->        MovieContract.NowPlayingMovies.CONTENT_ITEM_TYPE

            UPCOMING.LIST ->        MovieContract.UpcomingMovies.CONTENT_TYPE
            UPCOMING.ITEM ->        MovieContract.UpcomingMovies.CONTENT_ITEM_TYPE

            POPULAR.LIST  ->        MovieContract.MostPopularMovies.CONTENT_TYPE
            POPULAR.ITEM  ->        MovieContract.MostPopularMovies.CONTENT_ITEM_TYPE

            THEATERS_IDS.LIST ->    MovieContract.NowPlayingIds.CONTENT_TYPE
            THEATERS_IDS.ITEM ->    MovieContract.NowPlayingIds.CONTENT_ITEM_TYPE

            UPCOMING_IDS.LIST ->    MovieContract.UpcomingIds.CONTENT_TYPE
            UPCOMING_IDS.ITEM ->    MovieContract.UpcomingIds.CONTENT_ITEM_TYPE

            POPULAR_IDS.LIST  ->    MovieContract.MostPopularIds.CONTENT_TYPE
            POPULAR_IDS.ITEM  ->    MovieContract.MostPopularIds.CONTENT_ITEM_TYPE

            else-> throw badUri(uri)
        }
    }

    /**
     * Helper function used to obtain the table name based on the
     * given [uri]
     * @param [uri] The table URI
     * @return A [String] instance bearing the table name
     * @throws IllegalArgumentException if the received [uri] does not refer to an existing table
     */

    private fun resolveTableInfoFromUri(uri: Uri): String = when (uriMatcher.match(uri)) {
        DETAILS.LIST  ->     DbSchema.MovieDetails.TBL_NAME
        UPCOMING.LIST ->     DbSchema.UpcomingMovies.VIEW_NAME
        THEATERS.LIST ->     DbSchema.NowPlayingMovies.VIEW_NAME
        POPULAR.LIST  ->     DbSchema.MostPopularMovies.VIEW_NAME
        UPCOMING_IDS.LIST -> DbSchema.UpcomingIds.TBL_NAME
        THEATERS_IDS.LIST -> DbSchema.NowPlayingIds.TBL_NAME
        POPULAR_IDS.LIST  -> DbSchema.MostPopularIds.TBL_NAME
        else -> null
    } ?: throw badUri(uri)

    /**
     * Helper function used to obtain the table name and selection arguments based on the
     * given [uri]
     * @param [uri] The received URI, which may refer to a table or to an individual entry
     * @return A [Pair] instance bearing the table name (the Pair's first), the selection
     * string (the Pair's second)
     * @throws IllegalArgumentException if the received [uri] does not refer to a valid data set
     */
    private fun resolveTableAndSelectionInfoFromUri(uri: Uri, selection: String?): Pair<String, String?> {
        val itemSelection = "${BaseColumns._ID} = ${uri.lastPathSegment}"
        return when (uriMatcher.match(uri)) {
            DETAILS.ITEM  ->     Pair(DbSchema.MovieDetails.TBL_NAME, itemSelection)
            UPCOMING.ITEM ->     Pair(DbSchema.UpcomingMovies.VIEW_NAME, itemSelection)
            THEATERS.ITEM ->     Pair(DbSchema.NowPlayingMovies.VIEW_NAME, itemSelection)
            POPULAR.ITEM  ->     Pair(DbSchema.MostPopularMovies.VIEW_NAME, itemSelection)
            UPCOMING_IDS.ITEM -> Pair(DbSchema.UpcomingIds.TBL_NAME, itemSelection)
            THEATERS_IDS.ITEM -> Pair(DbSchema.NowPlayingIds.TBL_NAME, itemSelection)
            POPULAR_IDS.ITEM  -> Pair(DbSchema.MostPopularIds.TBL_NAME, itemSelection)
            else -> resolveTableInfoFromUri(uri).let { Pair(it, selection) }
        }
    }


    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        val params = resolveTableAndSelectionInfoFromUri(uri, selection)
        val qbuilder = SQLiteQueryBuilder()
        qbuilder.tables = params.first
        val db = dbHelper.readableDatabase
        val cursor = qbuilder.query(db, projection, null, null, null, null, sortOrder)
        //val cursor = qbuilder.query(db, projection, params.second, selectionArgs, null, null, sortOrder)
        cursor.setNotificationUri(context.contentResolver, uri)
        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val table = resolveTableAndSelectionInfoFromUri(uri, null).first
        val db = dbHelper.writableDatabase
        val newId = db.insert(table, null, values)

        if (!inBatchMode.get())
            context.contentResolver.notifyChange(uri, null)

        return ContentUris.withAppendedId(uri, newId)

    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        val table = resolveTableAndSelectionInfoFromUri(uri, selection).first
        val db = dbHelper.writableDatabase
        return db.update(table, values, selection, selectionArgs)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val table = resolveTableAndSelectionInfoFromUri(uri, null).first
        val db = dbHelper.writableDatabase

        val ndel = db.delete(table, null, null)
        if (ndel > 0 && !inBatchMode.get()) {
            context.contentResolver.notifyChange(uri, null)
        }
        return ndel
    }


    val inBatchMode = object : ThreadLocal<Boolean>() {
        override fun initialValue(): Boolean = false
    }

    override fun applyBatch(operations: ArrayList<ContentProviderOperation>?): Array<ContentProviderResult> {
        val database = dbHelper.writableDatabase
        inBatchMode.set(true)
        database.beginTransaction()
        val results : Array<ContentProviderResult>
        try {
            results = super.applyBatch(operations)
            database.setTransactionSuccessful()
        } finally {
            database.endTransaction()
            inBatchMode.set(false)
        }
        context.contentResolver.notifyChange(MovieContract.BASE_CONTENT_URI, null)
        return results
    }

    private fun badUri(uri: Uri?) =
            IllegalArgumentException("unknown uri: $uri")

}