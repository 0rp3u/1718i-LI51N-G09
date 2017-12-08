package pdm_1718i.yamda.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MoviesDbHelper(context: Context?) : SQLiteOpenHelper(context, DbSchema.DB_NAME, null, DbSchema.DB_VERSION){

    override fun onCreate(db: SQLiteDatabase?) {
        createDb(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        deleteDb(db)
        createDb(db)
        return
        TODO("refactor method so it migrate old db to new one")
    }

    private fun createDb(db: SQLiteDatabase?) {
        db?.execSQL(DbSchema.MovieDetails.DDL_CREATE_TABLE)
        db?.execSQL(DbSchema.NowPlayingMovies.DDL_CREATE_TABLE)
        db?.execSQL(DbSchema.UpcomingMovies.DDL_CREATE_TABLE)
        db?.execSQL(DbSchema.MostPopularMovies.DDL_CREATE_TABLE)
    }

    private fun deleteDb(db: SQLiteDatabase?) {
        db?.execSQL(DbSchema.MovieDetails.DDL_DROP_TABLE)
        db?.execSQL(DbSchema.NowPlayingMovies.DDL_DROP_TABLE)
        db?.execSQL(DbSchema.UpcomingMovies.DDL_DROP_TABLE)
        db?.execSQL(DbSchema.MostPopularMovies.DDL_DROP_TABLE)
    }
}