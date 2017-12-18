package pdm_1718i.yamda.data.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Build
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.utils.UtilPreferences
import pdm_1718i.yamda.ui.App
import pdm_1718i.yamda.ui.activities.MovieDetailActivity
import pdm_1718i.yamda.ui.activities.MovieDetailActivity.Companion.BUNDLE_ID_KEY


class FollowNotificationService: JobService(){

    companion object {
        val REQUEST_CODE = -2
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return false //no need to re-schedule the job
    }

    override fun onStartJob(jobParameters: JobParameters?): Boolean {
        if(UtilPreferences.getNotification()) return false
        val movie_id: Int = jobParameters?.extras?.getInt(JobNotification.BUNDLE_ID_KEY) ?: return false

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
                Notification.Builder(this@FollowNotificationService, NotificationChannel.DEFAULT_CHANNEL_ID)
            } else {
                Notification.Builder(this@FollowNotificationService)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
            }.apply {
                setContentTitle("New movie released")
                setSmallIcon(R.drawable.ic_video_camera_80s)
                setAutoCancel(true)

                //setPriority(Notification.PRIORITY_MAX)

                //insert intent in the future
                Intent(App.instance, MovieDetailActivity::class.java).let {
                    it.putExtra(BUNDLE_ID_KEY, movie_id)
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    PendingIntent.getActivity(App.instance, REQUEST_CODE, it, PendingIntent.FLAG_CANCEL_CURRENT)
                }.let{ setContentIntent(it)}

                movieJob?.await()?.run {
                    setContentText(this.title)
                }

                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(movie_id, build())
            }
        }
        return true //code is running asynchronously
    }

    private fun notify(id: Int, notification: Notification){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            with(getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager){
                this.notify()
            }
        }else{

        }
    }

}