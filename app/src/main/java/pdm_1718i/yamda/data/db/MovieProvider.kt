package pdm_1718i.yamda.data.db

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import pdm_1718i.yamda.data.db.def.MoviesDetailsContract

/**
 * Created by Edgar on 26/11/2017.
 */
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

    private val URI_MATCHER: UriMatcher

    init {
        URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        /*****************************************************************************/
        URI_MATCHER.addURI(MoviesInTheatersContract.AUTHORITY,
                MoviesInTheatersContract.Movies.RESOURCE,
                THEATERS.CURSOR)

        URI_MATCHER.addURI(MoviesInTheatersContract.AUTHORITY,
                MoviesInTheatersContract.Movies.RESOURCE + "/#",
                THEATERS.ITEM)

        /*****************************************************************************/
        URI_MATCHER.addURI(MoviesUpcomingContract.AUTHORITY,
                MoviesUpcomingContract.Movies.RESOURCE,
                UPCOMING.CURSOR)

        URI_MATCHER.addURI(MoviesUpcomingContract.AUTHORITY,
                MoviesUpcomingContract.Movies.RESOURCE + "/#",
                UPCOMING.ITEM)

        /*****************************************************************************/
        URI_MATCHER.addURI(MoviesDetailsContract.AUTHORITY,
                MoviesDetailsContract.Movies.RESOURCE,
                DETAILS.CURSOR)

        URI_MATCHER.addURI(MoviesDetailsContract.AUTHORITY,
                MoviesDetailsContract.Movies.RESOURCE + "/#",
                DETAILS.ITEM)

        /*****************************************************************************/
    }

    private var dbHelper: MoviesDbHelper? = null

    override fun insert(p0: Uri?, p1: ContentValues?): Uri {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun query(p0: Uri?, p1: Array<out String>?, p2: String?, p3: Array<out String>?, p4: String?): Cursor {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(p0: Uri?, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(p0: Uri?, p1: String?, p2: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(uri: Uri?): String {
        when(URI_MATCHER.match(uri))
        {
            THEATERS.CURSOR -> return MoviesInTheatersContract.Movies.CONTENT_TYPE
            THEATERS.ITEM   -> return MoviesInTheatersContract.Movies.CONTENT_ITEM_TYPE

            UPCOMING.CURSOR -> return MoviesUpcomingContract.Movies.CONTENT_TYPE
            UPCOMING.ITEM   -> return MoviesUpcomingContract.Movies.CONTENT_ITEM_TYPE

            DETAILS.CURSOR  -> return MoviesDetailsContract.Movies.CONTENT_TYPE
            DETAILS.ITEM    -> return MoviesDetailsContract.Movies.CONTENT_ITEM_TYPE

            else-> throw badUri(uri)
        }
    }
    private fun badUri(uri: Uri?) =
            IllegalArgumentException("unknown uri: $uri")

}