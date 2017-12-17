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
import pdm_1718i.yamda.data.ConnectivityManager
import pdm_1718i.yamda.data.MoviesProvider.Companion.tmdbAPI
import pdm_1718i.yamda.data.db.MovieContract
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.model.MovieDetail

class DatabaseUpdater : Service() {


    /** @see Service.onBind */
    override fun onBind(intent: Intent): IBinder? = null

    /** @see Service.onStartCommand */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        with(ConnectivityManager){

            if(isConnected() && isWifi())
                fetchDataToUpdate()
            else{
                //TODO("Schedule db sync for when device is connected")
            }
        }
        return Service.START_REDELIVER_INTENT
    }

    private fun fetchDataToUpdate() {
        /*val deleted = contentResolver.delete(MovieContract.MovieDetails.CONTENT_URI, null, null)
         Log.d("databa_updater", "deleted $deleted details from database")
        */
        val onDatabase: Set<Int> = moviesInDataBase()

        //Get DataBase Details ID's, so we do not fetch those
        //fetch upcoming, mostPopular and nowplaying movie ids
        //if any of those are already on database dont fetch them
        //delete stale data from DB, update other data


        async {


            val detailsToFetch: MutableSet<Int> = mutableSetOf() //concurrently updated with the movies that are already fetched

            val upcomingJob = bg { fetchUpcoming(onDatabase, detailsToFetch) }
            val nowPLayingJob = bg { fetchNowPlaying(onDatabase, detailsToFetch) }
            val mostPopularJob = bg { fetchMostPopular(onDatabase, detailsToFetch) }


            updateDb(
                    upcomingJob.await(),
                    nowPLayingJob.await(),
                    mostPopularJob.await(),
                    onDatabase.filter { detailsToFetch.contains(it) }.toSet(), //staled data
                    detailsToFetch.map {
                        bg { tmdbAPI.movieDetail(it) }
                    }.map {
                        try {
                            it.await()
                        } catch (e: Throwable) {
                            null
                        }
                    }.toSet()
            )
        }
    }

    private class MovieListEntry(val page: Int, val list: Set<Int>)

    private fun fetchMostPopular(onDatabase: Set<Int>, alreadyFetched: MutableSet<Int>): Set<MovieListEntry> {
        Log.d("databa_updater", "${Thread.currentThread().id} fetching most Popular movies ")
        return (1..5).map { fetchListEntry(it, tmdbAPI::popularMovies, onDatabase, alreadyFetched) }.toSet()
    }

    private fun fetchNowPlaying(onDatabase: Set<Int>, alreadyFetched: MutableSet<Int>): Set<MovieListEntry> {
        Log.d("databa_updater", "${Thread.currentThread().id} fetching most nowPlaying movies ")
        return (1..5).map { fetchListEntry(it, tmdbAPI::playingMovies, onDatabase, alreadyFetched) }.toSet()
    }

    private fun fetchUpcoming(onDatabase: Set<Int>, alreadyFetched: MutableSet<Int>): Set<MovieListEntry> {
        Log.d("databa_updater", "${Thread.currentThread().id} fetching upcoming movies ")
        return (1..5).map { fetchListEntry(it, tmdbAPI::upcomingMovies, onDatabase, alreadyFetched) }.toSet()
    }

    private fun fetchListEntry(page: Int, providerHandler: (page: Int) -> List<Movie>, onDatabase: Set<Int>, alreadyFetched: MutableSet<Int>): MovieListEntry {
        val entryIds: MutableSet<Int> = providerHandler(page).map { it.id }.toMutableSet()
        synchronized(alreadyFetched,{
            alreadyFetched.addAll( // remove all that are already on database, or on the list to fetch/fetched and add them to the list
                    entryIds
                    .filterNot { onDatabase.contains(it) }
                    .filterNot { alreadyFetched.contains(it) }
           )
        })
        return MovieListEntry(page, entryIds.toSet())
    }

    private fun moviesInDataBase(): Set<Int> {
        val cursor: Cursor = contentResolver.query(MovieContract.MovieDetails.CONTENT_URI, null, null, null, null)
        val ids = mutableSetOf<Int>()
        if (cursor.count > 0) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                ids.add(cursor.getInt(0))
                cursor.moveToNext()
            }

        }
        cursor.close()
        return ids.toSet()
    }

    private fun updateDb(
            upComing: Set<MovieListEntry>,
            nowPlaying: Set<MovieListEntry>,
            mostPopular: Set<MovieListEntry>,
            staled: Set<Int>,
            movieDetails: Set<MovieDetail?>) {
        contentResolver.delete(MovieContract.UpcomingIds.CONTENT_URI, null, null)
        contentResolver.delete(MovieContract.NowPlayingIds.CONTENT_URI, null, null)
        contentResolver.delete(MovieContract.MostPopularIds.CONTENT_URI, null, null)
        staled.forEach {
            contentResolver.delete(MovieContract.MovieDetails.CONTENT_URI, "$it", null)
        }


        val moviesContentValue: Array<ContentValues> =  movieDetails.mapNotNull {
            it?.toContentValues()
        }.toTypedArray()

        val upcomingContentValue: Array<ContentValues> = upComing.toContentValues()

        val mostPopularContentValue: Array<ContentValues> = mostPopular.toContentValues()

        val nowPlayingContentValue: Array<ContentValues> = nowPlaying.toContentValues()

        val upcomingCount = contentResolver.bulkInsert(MovieContract.UpcomingIds.CONTENT_URI, upcomingContentValue)
        Log.d("databa_updater", "updated $upcomingCount upcoming movies to the database")

        val mostPopularCount = contentResolver.bulkInsert(MovieContract.MostPopularIds.CONTENT_URI, mostPopularContentValue)
        Log.d("databa_updater", "updated $mostPopularCount MostPopular movies to the database")

        val nowPlayingCount = contentResolver.bulkInsert(MovieContract.NowPlayingIds.CONTENT_URI, nowPlayingContentValue)
        Log.d("databa_updater", "updated $nowPlayingCount nowPlaying movies to the database")

        val count = contentResolver.bulkInsert(MovieContract.MovieDetails.CONTENT_URI, moviesContentValue)
        Log.d("databa_updater", "updated $count movie details to the database")


    }

    private fun Set<MovieListEntry>.toContentValues() =
            fold(mutableSetOf()) { acc: MutableSet<ContentValues>, next ->
                synchronized(acc, {
                    acc.addAll(
                        next.list.map {
                            ContentValues().apply {
                                put(MovieContract.MovieListId.DETAILS_ID, it)
                                put(MovieContract.MovieListId.PAGE, next.page)
                            }
                        }
                    )
                })
                acc
            }.distinctBy { it.getAsInteger(MovieContract.MovieListId.DETAILS_ID) }.toTypedArray() //small hack, because different pages can have repeated movies

    private fun handleError(error: VolleyError): Unit {
        // TODO
        Log.v("DEMO", "KABBUMMM")
    }
}
