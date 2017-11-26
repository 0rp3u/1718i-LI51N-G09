package pdm_1718i.yamda.data.db

/**
 * Created by orpheu on 11/4/17.
 */
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

    class MoviesDbHelper(context: Context?) : SQLiteOpenHelper(context, DbSchema.DB_NAME, null, DbSchema.DB_VERSION){

        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(DbSchema.TrendingMovies.DDL_CREATE_TABLE)
            db?.execSQL(DbSchema.UpcoingMovies.DDL_CREATE_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

            TODO("migrate old db to new one")
        }
    }