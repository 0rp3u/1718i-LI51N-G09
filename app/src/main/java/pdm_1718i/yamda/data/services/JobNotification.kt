package pdm_1718i.yamda.data.services

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import pdm_1718i.yamda.ui.App
import java.util.*

object JobNotification{

    //val jobScheduler by lazy { App.instance.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler }

    private fun newInstance(id:Int, calendar: Calendar, context: Context): JobInfo{
        return JobInfo.Builder(
            id,
            ComponentName(App.instance.applicationContext, FollowNotificationService::class.java)
        ).run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setRequiresBatteryNotLow(true)  //check preferences
            }
            //setPersisted(true)
            //setMinimumLatency(calendar.FromPresentInMillis()) //wait at least
            setMinimumLatency(1000 * 2) //wait at least
            setOverrideDeadline(0)
            build()
        }
    }

    fun schedule(id: Int, calendar: Calendar, context: Context): Boolean{
        val jobScheduler = App.instance.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val result = jobScheduler.schedule(newInstance(id, calendar, context))
        if(result == JobScheduler.RESULT_SUCCESS){
            return true
        }else{
            return false
        }
//
//        return when(jobScheduler.schedule(newInstance(id, calendar))){
//            JobScheduler.RESULT_FAILURE -> false
//            JobScheduler.RESULT_SUCCESS -> true
//            else -> false
//        }
    }
}
//
//private fun newInstance(id:Int, calendar: Calendar): JobInfo{
//    return JobInfo.Builder(
//            id,
//            ComponentName(App.instance, FollowNotificationService::class.java)
//    ).run {
//        setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            setRequiresBatteryNotLow(true)
//        }
//        setPersisted(true)
//        setMinimumLatency(calendar.FromPresentInMillis()) //wait at least
//        build()
//    }
//}