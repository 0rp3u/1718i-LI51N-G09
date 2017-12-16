package pdm_1718i.yamda.data.services

import android.app.job.JobParameters
import android.app.job.JobService
import pdm_1718i.yamda.extensions.toast

class FollowNotificationService: JobService(){
    override fun onStopJob(p0: JobParameters?): Boolean {
        return false //no need to re-schedule the job
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        //TODO create system notification with Notification Manager
        toast("OnStartJob - Follow Service")
        return false //no async code as of now
    }

}