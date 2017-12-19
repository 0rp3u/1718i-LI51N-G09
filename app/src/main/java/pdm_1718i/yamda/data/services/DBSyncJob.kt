package pdm_1718i.yamda.data.services

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.os.PersistableBundle
import pdm_1718i.yamda.data.utils.UtilPreferences
import pdm_1718i.yamda.extensions.FromPresentInMillis
import pdm_1718i.yamda.extensions.getNetworkTypeFromPreferences
import pdm_1718i.yamda.ui.App
import java.util.*

object DBSyncJob{
    val BUNDLE_ID_KEY = "id"
    val JOB_ID_FORCE_UPDATE = -2
    val JOB_ID = -1

    private fun newInstance(calendar: Calendar?): JobInfo {
        val job_id = if(calendar != null) JOB_ID else JOB_ID_FORCE_UPDATE
        return JobInfo.Builder(
                JOB_ID,
                ComponentName(App.instance.applicationContext, DatabaseUpdater::class.java)
        ).run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setRequiresBatteryNotLow(!UtilPreferences.getLowBatteryUpdate())
            }

            setRequiredNetworkType(this.getNetworkTypeFromPreferences())
            setRequiresDeviceIdle(true)
            setPersisted(true)

            if(calendar != null){
                setPeriodic(calendar.FromPresentInMillis())
            }

            setExtras(PersistableBundle().apply {
                putInt(BUNDLE_ID_KEY, job_id)
            })
            build()
        }
    }

    fun schedule(calendar: Calendar? = null): Boolean{
        val jobScheduler = App.instance.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        return jobScheduler.schedule(newInstance(calendar))==JobScheduler.RESULT_SUCCESS
    }

}