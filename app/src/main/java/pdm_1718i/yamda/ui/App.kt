package pdm_1718i.yamda.ui

import android.app.Application
import com.android.volley.toolbox.Volley
import android.text.TextUtils
import com.android.volley.Request
import pdm_1718i.yamda.data.MoviesController


class App : Application() {

    companion object {
        lateinit var instance: App
            private set
        private val requestQueue by lazy { Volley.newRequestQueue(instance)}
        val moviesController by lazy { MoviesController()}
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