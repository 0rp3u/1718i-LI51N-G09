package pdm_1718i.yamda.data.services

import android.app.Service
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.os.IBinder
import android.util.Log
import com.android.volley.VolleyError
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import pdm_1718i.yamda.data.db.MovieContract
import pdm_1718i.yamda.model.DetailedMovie
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App


class DatabaseUpdater : Service() {


    /** @see Service.onBind */
    override fun onBind(intent: Intent): IBinder? = null

    /** @see Service.onStartCommand */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        fetchDataToUpdate()

        /*with(ConnectivityManager){

            if(isConnected() && isWifi()) fetchDataToUpdate()

        }*/
        return Service.START_REDELIVER_INTENT
    }

    private  fun fetchDataToUpdate(){
        val deleted = contentResolver.delete(MovieContract.MovieDetails.CONTENT_URI, null, null)
        Log.d("databa_updater", "deleted $deleted from database")

        val onDatabase : List<Int> = moviesInDataBase()

        //Get DataBase Details ID's, so we do not fetch those
        //fetch upcoming, mostPopular and nowplaying movie ids
        //if any of those are already on database dont fetch them
        //delete stale data from DB, update other data
        /*Log.d("TEST", "finish")
        async {
            val fetchingJobs = (1..200).map {  bg{ App.moviesProvider.movieDetail(315029+it) }}

            fetchingJobs.forEach { it.join() }
            Log.d("TEST", "finish")
        }*/



        async {
            val detailsToFetch : MutableSet<Int> = mutableSetOf() //concurrently updated with the movies that are already fetched

            val upcomingJob = bg{ fetchUpcoming(onDatabase,detailsToFetch) }
            val nowPLayingJob = bg{ fetchNowPlaying(onDatabase,detailsToFetch)}
            val mostPopularjob = bg{ fetchMostPopular(onDatabase,detailsToFetch)}


            updateDb(
                    upcomingJob.await(),
                    nowPLayingJob.await(),
                    mostPopularjob.await(),
                    onDatabase.filter { detailsToFetch.contains(it) }, //staled data
                    detailsToFetch.map{
                        bg { fetchDetail(it) }}.map {
                        try{
                            it.await()
                        }catch (e: Throwable){
                            null
                        }}
            )
        }
    }

    private fun fetchMostPopular(onDatabase: List<Int>, alreadyFetched : MutableSet<Int>): List<Int>{
        Log.d("databa_updater", "${Thread.currentThread().id} fetching most Popular movies ")

        val mostPopularList :MutableSet<Int> = App.moviesProvider.popularMovies(1).map { it.id }.toMutableSet()
        val fetchDetails = mostPopularList
                        .filterNot { onDatabase.contains(it) }
                        .filterNot { alreadyFetched.contains(it) }
        alreadyFetched.addAll(fetchDetails)
        Log.d("databa_updater", "${Thread.currentThread().id} added ${fetchDetails.size}")
        return mostPopularList.toList()

    }

    private fun fetchNowPlaying(onDatabase: List<Int>, alreadyFetched : MutableSet<Int>): List<Int>{

        Log.d("databa_updater", "${Thread.currentThread().id} fetching most nowPlaying movies ")

        val nowPlayingList :MutableSet<Int> = App.moviesProvider.nowPlayingMovies(1).map { it.id }.toMutableSet()

        val fetchDetails =  nowPlayingList
                .filterNot { onDatabase.contains(it) }
                .filterNot { alreadyFetched.contains(it) }
                 //list of running thread that are fetching movieDetails

        alreadyFetched.addAll( fetchDetails )
        Log.d("databa_updater", "${Thread.currentThread().id} added ${fetchDetails.size}")

        return  nowPlayingList.toList()

    }

    private fun fetchUpcoming(onDatabase: List<Int>, alreadyFetched : MutableSet<Int>): List<Int>{
        Log.d("databa_updater", "${Thread.currentThread().id} fetching upcoming movies ")

        val nowPlayingList :MutableSet<Int> = App.moviesProvider.upcomingMovies(1).map { it.id }.toMutableSet()

        val fetchDetails =  nowPlayingList
                .filterNot { onDatabase.contains(it) }
                .filter { !alreadyFetched.contains(it) }

        alreadyFetched.addAll( nowPlayingList)
        Log.d("databa_updater", "${Thread.currentThread().id} added ${fetchDetails.size}")

        return  nowPlayingList.toList()

    }

    private fun fetchDetail(id: Int) : DetailedMovie{
        return App.moviesProvider.movieDetail(id)
    }

    private fun moviesInDataBase() : List<Int> {
        val cursor : Cursor = contentResolver.query(MovieContract.MovieDetails.CONTENT_URI,null,null,null,null)
        val ids =  mutableListOf<Int>()
        if(cursor.count > 0){
            cursor.moveToFirst()
            while(!cursor.isAfterLast){
                ids.add(cursor.getInt(0))
                cursor.moveToNext()
            }

        }
        cursor.close()
        return ids.toList()
    }


    private fun updateDb(
            upComing: List<Int>,
            nowPlaying: List<Int>,
            mostPopular: List<Int>,
            staled: List<Int>,
            movieDetails: List<DetailedMovie?>)
    {
        contentResolver.delete(MovieContract.UpcomingMovies.CONTENT_URI, null, null)
        contentResolver.delete(MovieContract.NowPlayingMovies.CONTENT_URI, null, null)
        staled.forEach {
            contentResolver.delete(MovieContract.MovieDetails.CONTENT_URI,"$it", null)
        }

        val moviesContentValue: Array<ContentValues> =  movieDetails.mapNotNull {
                ContentValues().apply {

                        put(MovieContract.MovieDetails._ID, it!!.id)
                        put(MovieContract.MovieDetails.IS_FOLLOWING, false)
                        put(MovieContract.MovieDetails.POSTER_PATH, it!!.poster_path)
                        put(MovieContract.MovieDetails.TITLE, it!!.title)
                        put(MovieContract.MovieDetails.RELEASE_DATE, it!!.release_date.toString())
                        put(MovieContract.MovieDetails.OVERVIEW, it!!.overview)
                        put(MovieContract.MovieDetails.ORIGINAL_TITLE, "title")

                }
        }.toTypedArray()

        val count = contentResolver.bulkInsert(MovieContract.MovieDetails.CONTENT_URI, moviesContentValue)

        Log.d("databa_updater", "updated $count movies to the database")


    }
    private fun handleError(error: VolleyError): Unit {
        // TODO
        Log.v("DEMO", "KABBUMMM")
    }
}

