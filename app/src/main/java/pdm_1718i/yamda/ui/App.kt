package pdm_1718i.yamda.ui

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.android.volley.Request
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.MoviesProvider
import pdm_1718i.yamda.data.cache.bitmap.DiskLruImageCache2
import pdm_1718i.yamda.data.server.ThrottlePolicy
import pdm_1718i.yamda.data.server.ThrottledHttpStack
import pdm_1718i.yamda.data.services.DatabaseUpdater
import java.net.URL


class App : Application() {

    companion object {
        lateinit var instance: App
            private set
        val requestQueue by lazy {
            val throttledAuthorities = listOf(
                    URL("http://${instance.getString(R.string.TMDB_IMAGE_URL)}").authority,
                    URL("http://${instance.getString(R.string.TMDB_URL)}").authority)
            val apiThrottlePolicy = ThrottlePolicy(throttledAuthorities,3,40, 10000)
            val trottleStack = ThrottledHttpStack(listOf(apiThrottlePolicy))
            Volley.newRequestQueue(instance, trottleStack)

        }
        val imageLoader by lazy {
            //val cache = BitmapLruCache(DiskLruImageCache(this.instance, "imageCache"))
            val cache = DiskLruImageCache2(requestQueue.cache)
            ImageLoader(requestQueue,cache)
        }

        val moviesProvider by lazy { MoviesProvider() }
        private val TAG = App::class.java.simpleName
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



            val action = Intent(this, DatabaseUpdater::class.java)
            (getSystemService(ALARM_SERVICE) as AlarmManager).setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    0,
                    AlarmManager.INTERVAL_DAY,
                    PendingIntent.getService(this, 1, action, PendingIntent.FLAG_UPDATE_CURRENT)
            )


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