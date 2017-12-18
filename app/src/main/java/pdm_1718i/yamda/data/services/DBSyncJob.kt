package pdm_1718i.yamda.data.services

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.os.PersistableBundle
import pdm_1718i.yamda.data.utils.UtilPreferences
import pdm_1718i.yamda.extensions.getNetworkTypeFromPreferences
import pdm_1718i.yamda.ui.App
import java.util.*

object DBSyncJob{
    val BUNDLE_ID = "id"
    val JOB_ID = -1

    private fun newInstance(calendar: Calendar?): JobInfo {
        return JobInfo.Builder(
                JOB_ID,
                ComponentName(App.instance.applicationContext, DatabaseUpdater::class.java)
        ).run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setRequiresBatteryNotLow(!UtilPreferences.getLowBatteryUpdate())
            }

            setRequiredNetworkType(this.getNetworkTypeFromPreferences())
            setPersisted(true)

            if(calendar != null){
                //setMinimumLatency(calendar.FromPresentInMillis()) //todo change setMinimumLatency on release
                setMinimumLatency(1000 * 5)
            }

            setExtras(PersistableBundle().apply {
                putInt(BUNDLE_ID, JOB_ID)
            })
            build()
        }
    }

    fun schedule(calendar: Calendar? = null): Boolean{
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