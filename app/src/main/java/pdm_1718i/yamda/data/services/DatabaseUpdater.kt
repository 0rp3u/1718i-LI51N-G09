package pdm_1718i.yamda.data.services

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.ContentProviderOperation
import android.content.ContentValues
import android.database.Cursor
import android.graphics.Bitmap
import android.util.Log
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.bg
import pdm_1718i.yamda.data.MoviesProvider.Companion.tmdbAPI
import pdm_1718i.yamda.data.db.MovieContract
import pdm_1718i.yamda.data.server.ImageOption
import pdm_1718i.yamda.extensions.isFuture
import pdm_1718i.yamda.extensions.toMovieList
import pdm_1718i.yamda.extensions.toast
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.model.MovieDetail
import java.io.ByteArrayOutputStream


class DatabaseUpdater : JobService() {
    val TAG = this.javaClass.simpleName
    private lateinit var fetchDataToUpdateJob : Job

    override fun onStopJob(p0: JobParameters?): Boolean {
        toast("Sync cancelled")
        fetchDataToUpdateJob.cancel(/*Throwable("OnStopJob: Scheduling requirements are no longer being met.")*/)
        return true
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        toast("DBSync started")
        fetchDataToUpdateJob = fetchDataToUpdate(p0)
        return true
    }

    private fun fetchDataToUpdate(p0: JobParameters?) = launch {
        try {
            val moviesOnDatabase: List<Movie> = moviesInDataBase()
            val onDatabase: List<Int> = moviesOnDatabase.map { it.id }

            val detailsToFetch: MutableSet<Int> = mutableSetOf() //concurrently updated with the movies that are already fetched
            val upcomingJob = bg { fetchUpcoming(onDatabase, detailsToFetch) }
            val nowPLayingJob = bg { fetchNowPlaying(onDatabase, detailsToFetch) }
            val mostPopularJob = bg { fetchMostPopular(onDatabase, detailsToFetch) }
            val imageFetchJobs: MutableList<Pair<Deferred<Bitmap>, String>> = mutableListOf()

            updateStaledFollowing(moviesOnDatabase) //remove following from movies that where already notified

            updateDb(
                    upcomingJob.await(),
                    nowPLayingJob.await(),
                    mostPopularJob.await(),
                    moviesOnDatabase,
                    detailsToFetch.map {
                        bg { tmdbAPI.movieDetail(it) }
                    }.mapNotNull {
                        try {
                            val movie = it.await()
                            if(movie.poster_path.isNotEmpty()) {
                                imageFetchJobs.add(Pair(bg { tmdbAPI.movieImageSync(movie.poster_path, ImageOption.SMALL) }, movie.poster_path))
                            }
                            movie
                        } catch (e: Throwable) {
                            Log.d(TAG, "${e.message}")
                            null
                        }
                    }.toSet(),
                    imageFetchJobs.mapNotNull { try {Pair(it.first.await(),it.second) }catch (e:Exception){null}}

            )
        }catch(e : Exception){
              Log.e(TAG, "${e.message}")
        }finally {
            jobFinished(p0, false)
        }
    }

    private class MovieListEntry(val page: Int, val list: Set<Int>)

    private fun fetchMostPopular(onDatabase: List<Int>, alreadyFetched: MutableSet<Int>): Set<MovieListEntry> {
        Log.d(TAG, "${Thread.currentThread().id} fetching most Popular movies ")
        return (1..5).map { fetchListEntry(it, tmdbAPI::popularMovies, onDatabase, alreadyFetched) }.toSet()
    }

    private fun fetchNowPlaying(onDatabase: List<Int>, alreadyFetched: MutableSet<Int>): Set<MovieListEntry> {
        Log.d(TAG, "${Thread.currentThread().id} fetching most nowPlaying movies ")
        return (1..5).map { fetchListEntry(it, tmdbAPI::playingMovies, onDatabase, alreadyFetched) }.toSet()
    }

    private fun fetchUpcoming(onDatabase: List<Int>, alreadyFetched: MutableSet<Int>): Set<MovieListEntry> {
        Log.d(TAG, "${Thread.currentThread().id} fetching upcoming movies ")
        return (1..5).map { fetchListEntry(it, tmdbAPI::upcomingMovies, onDatabase, alreadyFetched) }.toSet()
    }

    private fun fetchListEntry(page: Int, providerHandler: (page: Int) -> List<Movie>, onDatabase: List<Int>, toFetched: MutableSet<Int>): MovieListEntry {
        val entryIds: MutableSet<Int> = providerHandler(page).map { it.id }.toMutableSet()
        synchronized(toFetched,{
            toFetched.addAll( // remove all that are already on database
                    entryIds
                        .filterNot { onDatabase.contains(it) }
           )
        })
        return MovieListEntry(page, entryIds.toSet())
    }

    private fun moviesInDataBase(): List<Movie> {
        val cursor: Cursor = contentResolver.query(MovieContract.MovieDetails.CONTENT_URI,MovieContract.MovieDetails.PROJECT_ALL, null, null, null)
        val inDB = cursor.toMovieList()

        return inDB ?: listOf()
    }

    private fun updateDb(
            upComing: Set<MovieListEntry>,
            nowPlaying: Set<MovieListEntry>,
            mostPopular: Set<MovieListEntry>,
            onDatabase: List<Movie>,
            movieDetails: Set<MovieDetail>,
            posters: List<Pair<Bitmap, String>>
    ) {

        val staled = staledMovies(onDatabase, nowPlaying, mostPopular, upComing)

        val deleteOps = ArrayList<ContentProviderOperation>()

        deleteOps.add(ContentProviderOperation.newDelete(MovieContract.UpcomingIds.CONTENT_URI).build())
        deleteOps.add(ContentProviderOperation.newDelete(MovieContract.NowPlayingIds.CONTENT_URI).build())
        deleteOps.add(ContentProviderOperation.newDelete(MovieContract.MostPopularIds.CONTENT_URI).build())
        deleteOps.addAll(
                staled.map {
                    ContentProviderOperation.newDelete(MovieContract.MovieDetails.CONTENT_URI)
                            .withSelection("${MovieContract.MovieDetails._ID} = ?", arrayOf("${it.id}"))
                            .build()
                }
        )
        val deleted = contentResolver.applyBatch(MovieContract.AUTHORITY, deleteOps).size
        Log.d(TAG, "applied ${deleteOps.size} delete opperaions, deleted $deleted")

        val moviesContentValue: Array<ContentValues> = movieDetails.map {
            it.toContentValues()
        }.toTypedArray()

        val upcomingContentValue: Array<ContentValues> = upComing.toContentValues()

        val mostPopularContentValue: Array<ContentValues> = mostPopular.toContentValues()

        val nowPlayingContentValue: Array<ContentValues> = nowPlaying.toContentValues()

        val upcomingCount = contentResolver.bulkInsert(MovieContract.UpcomingIds.CONTENT_URI, upcomingContentValue)
        Log.d(TAG, "updated $upcomingCount upcoming movies to the database")

        val mostPopularCount = contentResolver.bulkInsert(MovieContract.MostPopularIds.CONTENT_URI, mostPopularContentValue)
        Log.d(TAG, "updated $mostPopularCount MostPopular movies to the database")

        val nowPlayingCount = contentResolver.bulkInsert(MovieContract.NowPlayingIds.CONTENT_URI, nowPlayingContentValue)
        Log.d(TAG, "updated $nowPlayingCount nowPlaying movies to the database")

        val count = contentResolver.bulkInsert(MovieContract.MovieDetails.CONTENT_URI, moviesContentValue)
        Log.d(TAG, "updated $count movie details to the database")

       val postersContentValues = posters.map {
                    val bitmap = it.first
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val byteArray = stream.toByteArray()
                    stream.close()
                    ContentValues().apply {
                        put(MovieContract.Image._ID, it.second)
                        put(MovieContract.Image.BITMAP_DATA, byteArray)
                    }
            }.toTypedArray()

        val imgCount = contentResolver.bulkInsert(MovieContract.Image.CONTENT_URI, postersContentValues )
        Log.d(TAG, "updated $imgCount images to the database")

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


    private fun updateStaledFollowing(onDatabase : List<Movie>){
        val updateOps = ArrayList<ContentProviderOperation>()

        updateOps.addAll(
        onDatabase.filter{ it.isFollowing && it.release_date!!.isFuture().not() }.map {
            ContentProviderOperation.newUpdate(MovieContract.MovieDetails.CONTENT_URI)
                    .withSelection("${MovieContract.MovieDetails._ID} = ?", arrayOf("${it.id}"))
                    .withValues(ContentValues().apply { put(MovieContract.MovieDetails.IS_FOLLOWING, false)})
                    .build()
        })

        val updated = contentResolver.applyBatch(MovieContract.AUTHORITY,  updateOps).size
        Log.d(TAG, "applied ${updateOps.size} update following operations, updated $updated")
    }


    /**
     * returns the ids of the movies in the database that are not being followed
     * and are not in any of the lists
     */
    private fun staledMovies(
            onDatabase : List<Movie>,
            nowPlaying: Set<MovieListEntry>,
            mostPopular: Set<MovieListEntry>,
            upComing: Set<MovieListEntry>)
            = onDatabase.filter {
            val id = it.id
            it.isFollowing
            !(nowPlaying.find{ it.list.contains(id) } != null || mostPopular.find{ it.list.contains(id)} != null || upComing.find{ it.list.contains(id) } != null) //not on any of this lists
        }

}
