package pdm_1718i.yamda.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.android.volley.Request
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import pdm_1718i.yamda.data.MoviesProvider
import pdm_1718i.yamda.data.server.BitmapLruCache


class App : Application() {

    companion object {
        lateinit var instance: App
            private set
        private val requestQueue by lazy { Volley.newRequestQueue(instance) }
        val imageLoader by lazy { ImageLoader(requestQueue, BitmapLruCache()) }
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
    }

    fun <T> addToRequestQueue(request: Request<T>, tag: String = TAG) {
        request.tag = tag
        requestQueue.add(request)
    }

    fun cancelPendingRequests(tag: Any) {
        requestQueue.cancelAll(tag)
    }

}