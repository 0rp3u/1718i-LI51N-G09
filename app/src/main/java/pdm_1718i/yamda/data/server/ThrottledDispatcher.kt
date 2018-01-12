package pdm_1718i.yamda.data.server

import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpStack
import com.android.volley.toolbox.RequestFuture
import kotlinx.coroutines.experimental.launch
import org.apache.http.HttpResponse
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class DispatchRequest(val request: Request<*>?, val additionalHeaders: MutableMap<String, String>?) {
    val future: RequestFuture<HttpResponse> = RequestFuture.newFuture()//future that caller is waiting on
}

class ThrottledDispatcher(private val policy: ThrottlePolicy, private val httpStack: HttpStack) {

    private val requestsTimeStamps: ConcurrentLinkedQueue<Long> = ConcurrentLinkedQueue()
    private val requestQueue : LinkedBlockingQueue<DispatchRequest> = LinkedBlockingQueue()

    val dispatcher = thread(true, true, null, "dispatcher_Thread", Thread.NORM_PRIORITY, {
        Log.d("dispatcher_Thread ${Thread.currentThread().id}", "Dispatcher initialized")
        while (true) {
            val work = requestQueue.poll(60*10, TimeUnit.SECONDS)
            if(exceededTimeframe(policy.maxRequestsInTimeFrame)) {
                val waitTime = policy.timeFrame - (System.currentTimeMillis() - requestsTimeStamps.last())
                Log.d("dispatcher_Thread ${Thread.currentThread().id}", "reached API limit, going to wait for $waitTime milliseconds, next work is ${work.request?.url}")

                try { Thread.sleep(waitTime) }catch (e: InterruptedException) { Log.e("dispatcher_Thread", "${e.message}")}
                requestsTimeStamps.clear()
            }
            if(work!=null) {
                requestsTimeStamps.add(System.currentTimeMillis()) //register request timeStamp
                work.request?.retryPolicy = DefaultRetryPolicy(
                        policy.timeFrame,
                        policy.retries,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

                //on another thread so dispatcher do not block waiting for the http response while it could be dispatching more requests
                launch {
                    //Log.d("Launch ${Thread.currentThread().id}", "dispatching ${work.request?.url}")
                    try {
                        work.future.onResponse(httpStack.performRequest(work.request, work.additionalHeaders))
                    } catch (e: Exception) {
                        work.future.onErrorResponse(VolleyError(e.message))
                    }
                }
            }
        }
    })

    fun put(request: DispatchRequest): RequestFuture<HttpResponse> {
        requestQueue.put(request)
        return request.future
    }



    //removes all timeStamps that are older than 10 seconds, verify if there are more than max request still inside
    private fun exceededTimeframe(maxRequests: Int): Boolean {
        val currentTimestamp = System.currentTimeMillis()
        val older = requestsTimeStamps.filter { ((currentTimestamp - it) >= policy.timeFrame) }
        requestsTimeStamps.removeAll(older)//remove all older than time Frame
        return requestsTimeStamps.size >= maxRequests //If there are more or equal max request in the last
    }
}