package pdm_1718i.yamda.data.db

/**
 * Created by orpheu on 11/4/17.
 */
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import pdm_1718i.yamda.data.db.def.DbSchema

class MoviesDbHelper(context: Context?) : SQLiteOpenHelper(context, DbSchema.DB_NAME, null, DbSchema.DB_VERSION){

        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(DbSchemaMovieDetails.MovieDetails.DDL_CREATE_TABLE)
            db?.execSQL(DbSchemaMoviesInTheaters.inTheatersMovies.DDL_CREATE_TABLE)
            db?.execSQL(DbSchemaMoviesUpcoming.UpcomingMovies.DDL_CREATE_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

            TODO("migrate old db to new one")
        }
    }