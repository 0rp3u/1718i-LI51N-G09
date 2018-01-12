package pdm_1718i.yamda.data.server

import android.net.http.AndroidHttpClient
import android.os.Build
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.HttpClientStack
import com.android.volley.toolbox.HttpStack
import com.android.volley.toolbox.HurlStack
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

class ThrottlePolicy(val authority : String, val retries :Int, val maxRequestsInTimeFrame:Int, val timeFrame: Int)



/**
 *Wrapper over [HttpStack] that blocks requesting thread
 *  if the request is going to exceed the limit of requests the authority have on a time frame
 *  thread is only blocked the remaining time until it can proceed with the request
 */
class ThrottledHttpStack(policies :List<ThrottlePolicy>) : HttpStack{

        private val httpStack = if (Build.VERSION.SDK_INT >= 9) { HurlStack() } else { HttpClientStack(AndroidHttpClient.newInstance("Volley/0")) } //taken from Volley.newRequestQueue source code so it's consistent
        private val dispatchers : ConcurrentHashMap<String, ThrottledDispatcher> = ConcurrentHashMap() // map's authority to corresponding dispatcher

        init {
        policies.forEach {
            val dispatcher = ThrottledDispatcher(it, httpStack)
            dispatchers.put(it.authority, dispatcher)
        }

    }
    override fun performRequest(request: Request<*>?, additionalHeaders: MutableMap<String, String>?): org.apache.http.HttpResponse {
        val authority : String = URL(request?.url).authority

        return if(dispatchers.containsKey(authority)){
            try { dispatchers[authority]?.put(DispatchRequest(request,additionalHeaders))?.get()!! } catch (e: Exception){ Log.e("ThrottledHttpStack", e.message); throw e}

        }else{
            httpStack.performRequest(request, additionalHeaders)
        }
    }
}