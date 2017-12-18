package pdm_1718i.yamda.ui

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.MoviesProvider
import pdm_1718i.yamda.data.cache.bitmap.DiskLruImageCache
import pdm_1718i.yamda.data.cache.bitmap.DiskLruImageCacheJakeHarton
import pdm_1718i.yamda.data.server.ThrottlePolicy
import pdm_1718i.yamda.data.server.ThrottledHttpStack
import pdm_1718i.yamda.data.services.DBSyncJob
import pdm_1718i.yamda.data.services.DatabaseUpdater
import java.net.URL


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
            val cache = DiskLruImageCacheJakeHarton(this.instance, "imageCache")
            //val cache = DiskLruImageCache(requestQueue.cache)
            ImageLoader(requestQueue,cache)
        }

        val moviesProvider by lazy { MoviesProvider() }


        val isNetworkAvailable: Boolean
            get(){

                val conMgr = instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                with(conMgr.activeNetworkInfo){
                    return this != null && this.isConnected && this.isAvailable
                }
            }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        DBSyncJob.schedule()
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