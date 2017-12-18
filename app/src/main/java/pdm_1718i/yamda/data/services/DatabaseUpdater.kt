package pdm_1718i.yamda.data.services

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import com.android.volley.VolleyError
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import pdm_1718i.yamda.data.MoviesProvider.Companion.tmdbAPI
import pdm_1718i.yamda.data.db.MovieContract
import pdm_1718i.yamda.data.utils.UtilPreferences
import pdm_1718i.yamda.extensions.isFuture
import pdm_1718i.yamda.extensions.toMovieList
import pdm_1718i.yamda.extensions.toast
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.model.MovieDetail
import pdm_1718i.yamda.ui.App

class DatabaseUpdater : JobService() {
    override fun onStopJob(p0: JobParameters?): Boolean {
        //todo
        UtilPreferences.getPeriodicity()
        toast("DBSync ended")
        //re-schedule service
        return true
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        toast("DBSync started")
        fetchDataToUpdate()
        return true
    }

    private fun fetchDataToUpdate() {
        async {

            val moviesOnDatabase: Set<Movie> = moviesInDataBase()
            val onDatabase: Set<Int> = moviesOnDatabase.map{it.id}.toSet()

            val detailsToFetch: MutableSet<Int> = mutableSetOf() //concurrently updated with the movies that are already fetched
            val upcomingJob = bg { fetchUpcoming(onDatabase, detailsToFetch) }
            val nowPLayingJob = bg { fetchNowPlaying(onDatabase, detailsToFetch) }
            val mostPopularJob = bg { fetchMostPopular(onDatabase, detailsToFetch) }

            updateStaledFollowing(moviesOnDatabase) //remove following from movies that where already notified

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

    private fun moviesInDataBase(): Set<Movie> {
        val cursor: Cursor = contentResolver.query(MovieContract.MovieDetails.CONTENT_URI, arrayOf(MovieContract.MovieDetails._ID,MovieContract.MovieDetails.RELEASE_DATE,MovieContract.MovieDetails.IS_FOLLOWING ), null, null, null)
        val inDB = cursor.toMovieList()

        return inDB!!.toSet()
    }

    private fun updateDb(
            upComing: Set<MovieListEntry>,
            nowPlaying: Set<MovieListEntry>,
            mostPopular: Set<MovieListEntry>,
            staled: Set<Int>,
            movieDetails: Set<MovieDetail?>
    ) {

        contentResolver.delete(MovieContract.UpcomingIds.CONTENT_URI, null, null)
        contentResolver.delete(MovieContract.NowPlayingIds.CONTENT_URI, null, null)
        contentResolver.delete(MovieContract.MostPopularIds.CONTENT_URI, null, null)
        staled.forEach {
            contentResolver.delete(MovieContract.MovieDetails.CONTENT_URI, "${MovieContract.MovieDetails._ID} = ?", arrayOf("$it"))
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


    private fun updateStaledFollowing(onDatabase : Set<Movie>){
        onDatabase.filter{ it.isFollowing && it.release_date!!.isFuture().not() }.forEach {
            App.instance.contentResolver.update(
                    MovieContract.MovieDetails.CONTENT_URI,
                    ContentValues().apply { put(MovieContract.MovieDetails.IS_FOLLOWING, false)},
                    "${MovieContract.MovieDetails._ID} = ?",
                    arrayOf("${it.id}")
            )
        }
    }

    private fun handleError(error: VolleyError): Unit {
        // TODO
        Log.v("DEMO", "KABBUMMM")
    }

}
