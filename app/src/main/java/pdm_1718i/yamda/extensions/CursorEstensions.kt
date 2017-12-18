package pdm_1718i.yamda.extensions

import android.database.Cursor
import android.support.annotation.IdRes
import pdm_1718i.yamda.data.db.MovieContract
import pdm_1718i.yamda.model.Genre
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.model.MovieDetail
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


/**
 * Function that builds a [MovieDetail] instance from the given [Cursor]
 * @return The newly created [MovieDetail]
 */
/*
private fun Cursor.constructDetailedMovieItem(): MovieDetail {
    return MovieDetail(
            id =            getInt(getColumnIndex(MovieContract.MovieDetails._ID)),
            title =         getString(getColumnIndex(MovieContract.MovieDetails.TITLE)),
            //adult =       getFromColumn(MovieContract.MovieDetails.ADULT) != 0,
            budget =        getInt(getColumnIndex(MovieContract.MovieDetails.BUDGET)),
            overview =      getString(getColumnIndex(MovieContract.MovieDetails.OVERVIEW)),
            poster_path =   getString(getColumnIndex(MovieContract.MovieDetails.POSTER_PATH)),
            isFollowing = false,
            vote_average =  getFloat(getColumnIndex(MovieContract.MovieDetails.VOTE_AVERAGE)),
            backdrop_path = getString(getColumnIndex(MovieContract.MovieDetails.BACKDROP_PATH)),
            release_date =  getCalendar(getString(getColumnIndex(MovieContract.MovieDetails.RELEASE_DATE))),
            genres = listOf(Genre(getString(getColumnIndex(MovieContract.MovieDetails.GENRES))))
    )
}*/

private fun Cursor.constructDetailedMovieItem() = MovieDetail(
            id =            getFromColumn(MovieContract.MovieDetails._ID),
            title =         getFromColumn(MovieContract.MovieDetails.TITLE),
            //adult =       getFromColumn(MovieContract.MovieDetails.ADULT) != 0,
            budget =        getFromColumn(MovieContract.MovieDetails.BUDGET),
            overview =      getFromColumn(MovieContract.MovieDetails.OVERVIEW),
            poster_path =   getFromColumn(MovieContract.MovieDetails.POSTER_PATH),
            isFollowing =   getFromColumn(MovieContract.MovieDetails.IS_FOLLOWING),
            vote_average =  getFromColumn(MovieContract.MovieDetails.VOTE_AVERAGE),
            backdrop_path = getFromColumn(MovieContract.MovieDetails.BACKDROP_PATH),
            release_date =  getCalendar(getFromColumn(MovieContract.MovieDetails.RELEASE_DATE)),
            genres = listOf(Genre(getFromColumn(MovieContract.MovieDetails.GENRES))) //small hack, because we dont deal with genres as a list on database
    )

/**
 * Function that builds a [Movie] instance from the given [Cursor]
 * @return The newly created [Movie]
 */

/*
private fun Cursor.constructMovieItem(): Movie {
    val d = System.currentTimeMillis()
    val ret = Movie(id =       getInt(getColumnIndex(MovieContract.MovieDetails._ID)),
            title =         getString(getColumnIndex(MovieContract.MovieDetails.TITLE)),
            //adult =       getFromColumn(MovieContract.MovieDetails.ADULT) != 0,
            poster_path =   getString(getColumnIndex(MovieContract.MovieDetails.POSTER_PATH)),
            vote_average =  getDouble(getColumnIndex(MovieContract.MovieDetails.VOTE_AVERAGE)),
            release_date =  getCalendar(getString(getColumnIndex(MovieContract.MovieDetails.RELEASE_DATE))),
            backdrop_path = getString(getColumnIndex(MovieContract.MovieDetails.BACKDROP_PATH))
    )
    Log.d("TIME TEST", "took ${System.currentTimeMillis() - d}")
    return  ret
}
*/

private fun Cursor.constructMovieItem() = Movie(
            id =            getFromColumn(MovieContract.MovieDetails._ID),
            title =         getFromColumn(MovieContract.MovieDetails.TITLE),
            //adult =       getFromColumn(MovieContract.MovieDetails.ADULT) != 0,
            poster_path =   getFromColumn(MovieContract.MovieDetails.POSTER_PATH),
            vote_average =  getFromColumn(MovieContract.MovieDetails.VOTE_AVERAGE),
            release_date =  getCalendar(getFromColumn(MovieContract.MovieDetails.RELEASE_DATE)),
            backdrop_path = getFromColumn(MovieContract.MovieDetails.BACKDROP_PATH),
            isFollowing =   getFromColumn(MovieContract.MovieDetails.IS_FOLLOWING)
    )


/**
 * Helper taken from https://github.com/JetBrains/kotlin/blob/master/spec-docs/reified-type-parameters.md for more information
 * more info: https://gafter.blogspot.pt/2006/12/super-type-tokens.html
 * https://github.com/JetBrains/kotlin/blob/master/spec-docs/reified-type-parameters.md
 */
open class TypeLiteral<T>{
    val type: Type
        get() = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
}
inline fun <reified T> typeLiteral(): TypeLiteral<T> = object : TypeLiteral<T>() {} // here T is replaced with the actual type



/**
 * Function that returns the columns value from a [Cursor] verifying its type before calling the correspondent
 * cursor function
 * @return The item casted to corresponding [T]
 */
/*
inline fun <reified T> Cursor.getFromColumn(columnName : String): T{
    with(T::class.java) {
        return when (true) {
            isInstance("")              -> getString(getColumnIndex(columnName)) as T
            isInstance(1)               -> getInt(getColumnIndex(columnName)) as T
            isInstance(true)            -> getInt(getColumnIndex(columnName)).toBoolean() as T
            isInstance(Short.MIN_VALUE)      -> getShort(getColumnIndex(columnName)) as T
            isInstance(Float.MIN_VALUE)      -> getFloat(getColumnIndex(columnName)) as T
            isInstance(Double.MIN_VALUE)     -> getDouble(getColumnIndex(columnName)) as T
            isInstance(Long.MIN_VALUE)       -> getLong(getColumnIndex(columnName)) as T
            isInstance(ByteArray(0))    -> getBlob(getColumnIndex(columnName)) as T
            else -> throw Exception("${T::class.java.name} is not supported")
        }
    }
}
*/


inline fun <reified T> Cursor.getFromColumn(columnName : String) : T{
    val idx = getColumnIndex(columnName)
    return when(getType(idx)){
        Cursor.FIELD_TYPE_INTEGER -> {
            val value = getInt(idx)
            if(T::class.java.isInstance(Int.MIN_VALUE)) value
            else value.toBoolean()
        }
        Cursor.FIELD_TYPE_STRING -> getString(idx)
        Cursor.FIELD_TYPE_FLOAT -> {
            val value =  getFloat(idx)
            if(T::class.java.isInstance(Float.MIN_VALUE)) value
            else value.toDouble()
        }
        Cursor.FIELD_TYPE_BLOB -> getBlob(idx)

        Cursor.FIELD_TYPE_NULL -> 0
        else -> throw Exception("type is not supported")
    } as T
}


/**
using type literal from https://github.com/JetBrains/kotlin/blob/master/spec-docs/reified-type-parameters.md
probably higher overhead
**/
/*
inline fun <reified T> Cursor.getFromColumn(columnName : String): T{
        return when (T::class.java) {
            typeLiteral<String>().type -> getString(getColumnIndex(columnName)) as T
            typeLiteral<Int>().type -> getInt(getColumnIndex(columnName)) as T
            typeLiteral<Short>().type -> getShort(getColumnIndex(columnName)) as T
            typeLiteral<Float>().type -> getFloat(getColumnIndex(columnName)) as T
            typeLiteral<Double>().type -> getDouble(getColumnIndex(columnName)) as T
            typeLiteral<Long>().type -> getLong(getColumnIndex(columnName)) as T
            typeLiteral<ByteArray>().type -> getBlob(getColumnIndex(columnName)) as T
            else -> throw Exception("${T::class.java.name} is not supported")
    }
}*/

/**
 * Function that verifies if a [cursor] has an item and builds a [MovieDetail] instance from it
 * @return The newly created [MovieItem]
 */
fun Cursor.toDetailedMovieItem(): MovieDetail? {
    use {
        return if (count > 0) {
            moveToFirst()
            return constructDetailedMovieItem()

        } else null
    }
}

/**
 * Extension function that builds a list of [Movie] from the given [Cursor]
 * @return The newly created list of [Movie]
 */
fun Cursor.toMovieList(): List<Movie>? {
    use {
        return if (count > 0) {
            val cursorIterator = object : AbstractIterator<Movie>() {
                override fun computeNext() {
                    when (isAfterLast) {
                        true -> done()
                        false -> setNext(constructMovieItem())
                    }
                    moveToNext()
                }
            }
            moveToFirst()
            return mutableListOf<Movie>().let {
                it.addAll(Iterable { cursorIterator })
                it
            }
        } else null
    }
}


