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
import pdm_1718i.yamda.model.MovieDetail
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

        async {
            val detailsToFetch : MutableSet<Int> = mutableSetOf() //concurrently updated with the movies that are already fetched

            //TODO details to fetch needs to be Thread-Safe here
            val upcomingJob =       bg{ fetchUpcoming(onDatabase,detailsToFetch) }
            val nowPLayingJob =     bg{ fetchNowPlaying(onDatabase,detailsToFetch)}
            val mostPopularjob =    bg{ fetchMostPopular(onDatabase,detailsToFetch)}

            //relatorio: não faz fetch dos filmes que já se encontram na tabela details, não fazendo
            // update à informação dos filmes em si, mas apenas no sentido em que vai buscar os
            // filmes que ainda não existiam na BD e remove os que já não fazem parte da lista, com
            // excepção aos Following.
            updateDb(
                    upcomingJob.await(),
                    nowPLayingJob.await(),
                    mostPopularjob.await(),
                    onDatabase.filter { detailsToFetch.contains(it) }, //staled data
                    detailsToFetch.map{ bg { fetchDetail(it)}}.map {
                        try{it.await()}
                        catch (e: Throwable){null}
                    }
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
                .filterNot { onDatabase.contains(it)}
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

    private fun fetchDetail(id: Int) : MovieDetail {
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
            movieDetails: List<MovieDetail?>)
    {
        contentResolver.delete(MovieContract.UpcomingIds.CONTENT_URI, null, null)
        contentResolver.delete(MovieContract.NowPlayingIds.CONTENT_URI, null, null)
        contentResolver.delete(MovieContract.MostPopularIds.CONTENT_URI, null, null)
        staled.forEach {
            contentResolver.delete(MovieContract.MovieDetails.CONTENT_URI,"$it", null)
        }

        val moviesContentValue: Array<ContentValues> =  movieDetails.mapNotNull {
               it?.toContentValues()
        }.toTypedArray()

        val upcomingContentValue: Array<ContentValues> =  upComing.mapNotNull {
            ContentValues().apply {
                put(MovieContract.UpcomingIds.DETAILS_ID, it)
                put(MovieContract.UpcomingIds.PAGE, 1)
            }
        }.toTypedArray()

        val mostPopularContentValue: Array<ContentValues> =  mostPopular.mapNotNull {
            ContentValues().apply {
                put(MovieContract.MostPopularIds.DETAILS_ID, it)
                put(MovieContract.MostPopularIds.PAGE, 1)
            }
        }.toTypedArray()

        val nowPlayingContentValue: Array<ContentValues> =  nowPlaying.mapNotNull {
            ContentValues().apply {
                put(MovieContract.NowPlayingIds.DETAILS_ID, it)
                put(MovieContract.NowPlayingIds.PAGE, 1)
            }
        }.toTypedArray()

        val upcomingCount = contentResolver.bulkInsert(MovieContract.UpcomingIds.CONTENT_URI, upcomingContentValue)
        Log.d("databa_updater", "updated $upcomingCount upcoming movies to the database")

        val mostPopularCount = contentResolver.bulkInsert(MovieContract.MostPopularIds.CONTENT_URI, mostPopularContentValue)
        Log.d("databa_updater", "updated $mostPopularCount upcoming movies to the database")

        val nowPlayingCount = contentResolver.bulkInsert(MovieContract.NowPlayingIds.CONTENT_URI, nowPlayingContentValue)
        Log.d("databa_updater", "updated $nowPlayingCount upcoming movies to the database")

        val count = contentResolver.bulkInsert(MovieContract.MovieDetails.CONTENT_URI, moviesContentValue)
        Log.d("databa_updater", "updated $count movie details to the database")


    }

    private fun handleError(error: VolleyError): Unit {
        // TODO
        Log.v("DEMO", "KABBUMMM")
    }
}

