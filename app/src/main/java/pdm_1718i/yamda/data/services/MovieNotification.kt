package pdm_1718i.yamda.data.services

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.PersistableBundle
import pdm_1718i.yamda.extensions.FromPresentInMillis
import pdm_1718i.yamda.ui.App
import java.util.*

object MovieNotification {

    val BUNDLE_ID_KEY = "id"

    private fun newInstance(id:Int, calendar: Calendar?): JobInfo{
        return JobInfo.Builder(
            id,
            ComponentName(App.instance.applicationContext, FollowNotificationService::class.java)
        ).run {
            if(calendar != null){
                setMinimumLatency(calendar.FromPresentInMillis()) //wait at least
                setOverrideDeadline(calendar.apply{add(Calendar.HOUR_OF_DAY, 1)}.FromPresentInMillis()) // agendar para até 1 hora depois do minLatency
            }

            setExtras(PersistableBundle().apply {
                putInt(BUNDLE_ID_KEY, id)
            })
            build()
        }
    }

    fun schedule(id: Int, calendar: Calendar? = null): Boolean{
        val jobScheduler = App.instance.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        return jobScheduler.schedule(newInstance(id, calendar)) == JobScheduler.RESULT_SUCCESS
    }

    fun cancel(id: Int){
        with(App.instance.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler){
            cancel(id)
        }
    }
}