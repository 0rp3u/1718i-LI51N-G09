package pdm_1718i.yamda.data.services

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.PersistableBundle
import pdm_1718i.yamda.ui.App
import java.util.*

object JobNotification{

    val BUNDLE_ID_KEY = "id"

    private fun newInstance(id:Int, calendar: Calendar): JobInfo{
        return JobInfo.Builder(
            id,
            ComponentName(App.instance.applicationContext, FollowNotificationService::class.java)
        ).run {
            //todo change setMinimumLatency on release
            //setMinimumLatency(calendar.FromPresentInMillis()) //wait at least
            setMinimumLatency(1000 * 5) //wait at least

            setExtras(PersistableBundle().apply {
                putInt(BUNDLE_ID_KEY, id)
            })
            build()
        }
    }

    fun schedule(id: Int, calendar: Calendar): Boolean{
        val jobScheduler = App.instance.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        return when(jobScheduler.schedule(newInstance(id, calendar))){
            JobScheduler.RESULT_SUCCESS -> true
            else -> false
        }
    }

    fun cancel(id: Int){
        with(App.instance.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler){
            cancel(id)
        }
    }
}