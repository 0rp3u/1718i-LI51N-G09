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

/*
    this is a test throttled dispatcher,
    might be susceptible to dead locks or race conditions!!
*/

class DispatchableRequest(val request: Request<*>?, val additionalHeaders: MutableMap<String, String>?) {
    val future: RequestFuture<HttpResponse> = RequestFuture.newFuture()//future that caller is waiting
}

class ThrottledDispatcher(private val policy: ThrottlePolicy, private val httpStack: HttpStack) {

    private val requestsTimeStamps: ConcurrentLinkedQueue<Long> = ConcurrentLinkedQueue()
    private val requestQueue : LinkedBlockingQueue<DispatchableRequest> = LinkedBlockingQueue()

    val dispatcher = thread(true, false, null, "dispatcher_Thread", -1, {
        Log.d("dispatcher_Thread ${Thread.currentThread().id}", "Dispatcher initialized")
        while (true) {
                val work = requestQueue.poll(60*10, TimeUnit.SECONDS)
            Log.d("dispatcher_Thread ${Thread.currentThread().id}", "There is work to do")

            if(execededTimeframe(policy.maxRequestsinTimeFrame)) {
                    val waitTime = policy.timeFrame - (System.currentTimeMillis() - requestsTimeStamps.last())
                    Log.d("dispatcher_Thread ${Thread.currentThread().id}", "reached API limit, going to wait for $waitTime miliseconds")
                    Thread.sleep(waitTime)
                    requestsTimeStamps.clear()
                }
            if(work!=null) {
                requestsTimeStamps.add(System.currentTimeMillis()) //register request timeStamp
                work.request?.retryPolicy = DefaultRetryPolicy(
                        policy.timeFrame,
                        policy.retries,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

                //on another thread so dispatcher do not block waiting for http response
                launch {
                    Log.d("Launch ${Thread.currentThread().id}", "dispatching ${work.request?.url}")
                    try {
                        work.future.onResponse(httpStack.performRequest(work.request, work.additionalHeaders))
                    } catch (e: Exception) {
                        work.future.onErrorResponse(VolleyError(e.message))
                    }
                }
            }
        }
    })

    fun put(request: DispatchableRequest): RequestFuture<HttpResponse> {
        requestQueue.put(request)
        return request.future
    }



    //removes all timeStamps that are older than 10 seconds, verify if there are more than max request still inside
    private fun execededTimeframe(maxRequests: Int): Boolean {
        val currentTimestamp = System.currentTimeMillis()
        val older = requestsTimeStamps.filter { ((currentTimestamp - it) >= policy.timeFrame) }
        requestsTimeStamps.removeAll(older)//remove all older than time Frame
        return requestsTimeStamps.size >= maxRequests //If there are more or equal max request in the last
    }
}