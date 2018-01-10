package pdm_1718i.yamda.ui

import android.app.Application
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.MoviesProvider
import pdm_1718i.yamda.data.cache.bitmap.DiskLruImageCache
import pdm_1718i.yamda.data.server.ThrottlePolicy
import pdm_1718i.yamda.data.server.ThrottledHttpStack
import pdm_1718i.yamda.data.services.DBSyncJob
import pdm_1718i.yamda.data.utils.UtilPreferences
import pdm_1718i.yamda.data.utils.UtilPreferences.isFirstInstance
import pdm_1718i.yamda.data.utils.UtilPreferences.updateIsFirstInstance
import pdm_1718i.yamda.extensions.AddHoursToPresent
import pdm_1718i.yamda.extensions.caseTrue
import pdm_1718i.yamda.extensions.getDate
import java.net.URL
import java.util.*


class App : Application() {

    companion object {
        private val TAG = App::class.java.simpleName

        lateinit var instance: App
            private set

        private val requestQueue: RequestQueue by lazy {
            val apiThrottlePolicy = ThrottlePolicy(
                    URL("http://${instance.getString(R.string.TMDB_URL)}").authority
                    ,3,39, 10000)
            val throttleStack = ThrottledHttpStack(listOf(apiThrottlePolicy))
            Volley.newRequestQueue(instance, throttleStack)
        }

        val imageLoader by lazy {
            //val cache = DiskLruImageCacheJakeHarton(this.instance, "imageCache")
            val cache = DiskLruImageCache(requestQueue.cache)
            ImageLoader(requestQueue,cache)
        }

        val moviesProvider by lazy { MoviesProvider() }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        isFirstInstance().caseTrue {
            updateIsFirstInstance()
            DBSyncJob.schedule() //schedule a non repeating update now so user has data on DB
            val hours = UtilPreferences.getPeriodicity()
            val calendar = Calendar.getInstance().AddHoursToPresent(hours)
            Log.v(TAG, "${calendar.getDate()}")
            DBSyncJob.schedule(calendar)
        }
    }

    fun <T> addToRequestQueue(request: Request<T>, tag: String = TAG) {
        request.tag = tag
        requestQueue.add(request)
        request.cacheKey

    }

    fun cancelPendingRequests(tag: Any) {
        requestQueue.cancelAll(tag)
    }

}