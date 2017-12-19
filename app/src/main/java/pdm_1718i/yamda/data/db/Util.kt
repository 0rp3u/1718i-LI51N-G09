package pdm_1718i.yamda.data.db

import android.content.ContentValues
import android.net.Uri
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.services.MovieNotification
import pdm_1718i.yamda.extensions.isFuture
import pdm_1718i.yamda.extensions.toast
import pdm_1718i.yamda.model.MovieDetail
import pdm_1718i.yamda.ui.App

object Util{
    val FOLLOW = true
    val UNFOLLOW = false
    val ERROR_FOLLOW by lazy { App.instance.getString(R.string.toast_follow_error) }

    fun updateFollowingState(movie:MovieDetail, newState: Boolean) : Boolean?{
        val cr = App.instance.contentResolver
        val MOVIE_ID = Integer.toString(movie.id)
        val uri = MovieContract.MovieDetails.CONTENT_URI
        val movieUri = Uri.withAppendedPath(uri, MOVIE_ID)
        val contentValues = ContentValues().apply {
            put(MovieContract.MovieDetails.IS_FOLLOWING, newState)
        }
        val where = "${MovieContract.MovieDetails._ID} = ?"
        val selectionArgs: Array<String> = arrayOf(MOVIE_ID)
        val nchanged = cr.update(movieUri, contentValues, where, selectionArgs)

        /** Schedule Job **/
        if(nchanged > 0){
            when(newState){
                FOLLOW -> {
                    movie.release_date?.run{
                        if(isFuture()) MovieNotification.schedule(movie.id, this)
                    }
                }

                UNFOLLOW ->{
                    MovieNotification.cancel(movie.id)
                }
            }
            return newState
        }else{
            App.instance.toast(ERROR_FOLLOW)
            return null
        }
    }
}