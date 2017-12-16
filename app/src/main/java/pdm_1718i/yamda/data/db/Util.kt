package pdm_1718i.yamda.data.db

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import pdm_1718i.yamda.data.services.JobNotification
import pdm_1718i.yamda.extensions.isFuture
import pdm_1718i.yamda.model.MovieDetail
import pdm_1718i.yamda.ui.App

object Util{

    fun updateFollowingState(movie:MovieDetail, newState: Boolean, context: Context) : Boolean?{
        //question: use application context or pass caller context by parameter?
        val cr = App.instance.contentResolver
        val MOVIE_ID = Integer.toString(movie.id)
        val uri = MovieContract.MovieDetails.CONTENT_URI
        val movieUri = Uri.withAppendedPath(uri, MOVIE_ID)
        val contentValues = ContentValues().apply {
            put(MovieContract.MovieDetails.IS_FOLLOWING, newState)
        }
        val where = "_ID = ?"
        val selectionArgs: Array<String> = arrayOf(MOVIE_ID)
        val nchanged = cr.update(movieUri, contentValues, where, selectionArgs)

        /** Schedule Job **/
        if(nchanged > 0){
            with(movie.release_date){
                if(this != null && isFuture()){
                    val scheduled = JobNotification.schedule(movie.id, this, context)
                    if(scheduled) Log.d("JobNotification", "Scheduled with success!")
                }
            }
            return true
        }else{
            return null
        }
    }
}