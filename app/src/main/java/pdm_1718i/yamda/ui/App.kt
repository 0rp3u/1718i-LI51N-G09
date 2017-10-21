package pdm_1718i.yamda.ui

import android.app.Application
import android.text.TextUtils
import com.android.volley.Request
import com.android.volley.toolbox.Volley
import pdm_1718i.yamda.data.MoviesProvider


class App : Application() {

    companion object {
        lateinit var instance: App
            private set
        private val requestQueue by lazy { Volley.newRequestQueue(instance)}
        val moviesProvider by lazy { MoviesProvider()}
        private val TAG = App::class.java.simpleName
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun <T> addToRequestQueue(request: Request<T>, tag: String) {
        request.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        requestQueue.add(request)
    }

    fun <T> addToRequestQueue(request: Request<T>) {
        request.tag = TAG
        requestQueue.add(request)
    }

    fun cancelPendingRequests(tag: Any) {
            requestQueue.cancelAll(tag)
    }

}