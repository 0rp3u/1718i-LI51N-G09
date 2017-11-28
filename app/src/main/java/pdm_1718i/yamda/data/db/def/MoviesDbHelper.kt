package pdm_1718i.yamda.data.db.def

/**
 * Created by orpheu on 11/4/17.
 */
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MoviesDbHelper(context: Context?) : SQLiteOpenHelper(context, DbSchema.DB_NAME, null, DbSchema.DB_VERSION){

        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(DbSchema.MovieDetails.DDL_CREATE_TABLE)
            db?.execSQL(DbSchema.NowPlayingMovies.DDL_CREATE_TABLE)
            db?.execSQL(DbSchema.UpcomingMovies.DDL_CREATE_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

            TODO("migrate old db to new one")
        }
    }