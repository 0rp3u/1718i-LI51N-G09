package pdm_1718i.yamda.data.services

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.PersistableBundle
import pdm_1718i.yamda.extensions.getNetworkTypeFromPreferences
import pdm_1718i.yamda.ui.App
import java.util.*

object DBSyncJob{
    val BUNDLE_ID = "id"
    val JOB_ID = -1

    private fun newInstance(calendar: Calendar): JobInfo {
        return JobInfo.Builder(
                JOB_ID,
                ComponentName(App.instance.applicationContext, DatabaseUpdater::class.java)
        ).run {
            //todo meter depois do edgar fazer a preference
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                setRequiresBatteryNotLow(true)  //check preferences
//            }

            setRequiredNetworkType(this.getNetworkTypeFromPreferences())
            setPersisted(true)
            //setMinimumLatency(calendar.FromPresentInMillis()) //todo change setMinimumLatency on release
            setMinimumLatency(1000 * 5) //wait at least
            
            setExtras(PersistableBundle().apply {
                putInt(BUNDLE_ID, JOB_ID)
            })
            build()
        }
    }

    fun schedule(id: Int, calendar: Calendar): Boolean{
        val jobScheduler = App.instance.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        return when(jobScheduler.schedule(newInstance(calendar))){
            JobScheduler.RESULT_SUCCESS -> true
            else -> false
        }
    }

    fun cancel(){
        with(App.instance.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler){
            cancel(JOB_ID)
        }
    }
}