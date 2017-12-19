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

    override fun onStopJob(jobParameters: JobParameters?): Boolean {
        //Job was cancelled
        jobParameters?.extras?.getInt(MovieNotification.BUNDLE_ID_KEY) ?: return false
        return true //re-schedule the job
    }

    override fun onStartJob(jobParameters: JobParameters?): Boolean {
        if(!UtilPreferences.getNotification()) return false
        val movie_id: Int = jobParameters?.extras?.getInt(MovieNotification.BUNDLE_ID_KEY) ?: return false

        async{
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
                Notification.Builder(this@FollowNotificationService).apply {
                    if(!UtilPreferences.getVibration()){
                        //setDefaults(getVibrationFromPreferences())
                        setVibrate(LongArray(1, {0}))
                    }else{
                        setVibrate(LongArray(2, {1000}))
                    }
                    this
                }
            }.apply {
                setContentTitle("New movie released")
                setSmallIcon(R.drawable.ic_video_camera_80s)
                setAutoCancel(true)

                //insert intent in the future
                Intent(App.instance, MovieDetailActivity::class.java).let {
                    it.putExtra(BUNDLE_ID_KEY, movie_id)
                    PendingIntent.getActivity(App.instance, movie_id, it, PendingIntent.FLAG_CANCEL_CURRENT)
                }.let{ setContentIntent(it)}

                movieJob?.await()?.run {
                    setContentText(this.title)
                }

                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(movie_id, build())

                jobFinished(jobParameters, false)
            }
        }
        return true //code is running asynchronously
    }

}