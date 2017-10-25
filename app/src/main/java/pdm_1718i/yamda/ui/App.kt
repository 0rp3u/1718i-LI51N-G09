package pdm_1718i.yamda.ui

import android.app.Application
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