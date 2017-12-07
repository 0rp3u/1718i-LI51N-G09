package pdm_1718i.yamda.data.server

import android.net.http.AndroidHttpClient
import android.os.Build
import com.android.volley.Request
import com.android.volley.toolbox.HttpClientStack
import com.android.volley.toolbox.HttpStack
import com.android.volley.toolbox.HurlStack
import java.net.URL
import java.util.concurrent.ConcurrentHashMap


class ThrottlePolicy(val authorities : List<String>, val retries :Int, val maxRequestsinTimeFrame :Int, val timeFrame: Int)

class ThrottledHttpStack(policies :List<ThrottlePolicy>) : HttpStack{

        private val httpStack = if (Build.VERSION.SDK_INT >= 9) { HurlStack() } else { HttpClientStack(AndroidHttpClient.newInstance("Volley/0")) }
        private val dispatchers : ConcurrentHashMap<String, ThrottledDispatcher> = ConcurrentHashMap()

        init {
        policies.forEach {
            val dispatcher = ThrottledDispatcher(it, httpStack)
            it.authorities.forEach {dispatchers.put(it, dispatcher)}
        }

    }
    override fun performRequest(request: Request<*>?, additionalHeaders: MutableMap<String, String>?): org.apache.http.HttpResponse {
        val authority : String = URL(request?.url).authority

        return if(dispatchers.containsKey(authority)){
            dispatchers[authority]?.put(DispatchableRequest(request,additionalHeaders))?.get()!!
        }else{
            httpStack.performRequest(request, additionalHeaders)
        }

    }


}