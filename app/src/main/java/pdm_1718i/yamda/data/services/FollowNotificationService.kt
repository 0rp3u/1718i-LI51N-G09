package pdm_1718i.yamda.data.services

import android.app.Notification
import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.os.Build
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import pdm_1718i.yamda.R
import pdm_1718i.yamda.ui.App


class FollowNotificationService: JobService(){
    override fun onStopJob(p0: JobParameters?): Boolean {
        return false //no need to re-schedule the job
    }

    override fun onStartJob(jobParameters: JobParameters?): Boolean {
        val movie_id: Int = jobParameters?.extras?.getInt(JobNotification.BUNDLE_ID) ?: return false

        async {
            val movieJob = bg {
                try {
                    App.moviesProvider.movieDetail(movie_id)
                } catch (e: Throwable) {
                    null
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //todo create Notification Channel
                Notification.Builder(this@FollowNotificationService, "")
            } else {
                Notification.Builder(this@FollowNotificationService)
            }.apply {
                setContentTitle("New movie released")
                setSmallIcon(R.drawable.ic_video_camera_80s)
                setAutoCancel(false)
                //insert intent in the future
                movieJob?.await()?.run {
                    setContentText(this.title)
                }

                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(movie_id, build())
            }
        }
        return true //code is running asynchronously
    }

}