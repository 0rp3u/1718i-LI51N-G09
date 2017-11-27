package pdm_1718i.yamda.data.db

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

class MoviesContentProvider: ContentProvider(){
    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        throw NotImplementedError()
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        throw NotImplementedError()
    }

    override fun onCreate(): Boolean {
        throw NotImplementedError()
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        throw NotImplementedError()
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        throw NotImplementedError()
    }

    override fun getType(uri: Uri?): String {
        throw NotImplementedError()
    }
}