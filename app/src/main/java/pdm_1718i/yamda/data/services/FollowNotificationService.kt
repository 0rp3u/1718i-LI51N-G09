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
import pdm_1718i.yamda.data.server.Options
import pdm_1718i.yamda.extensions.toast
import pdm_1718i.yamda.ui.App


class FollowNotificationService: JobService(){
    override fun onStopJob(p0: JobParameters?): Boolean {
        return false //no need to re-schedule the job
    }

    override fun onStartJob(jobParameters: JobParameters?): Boolean {
        toast("OnStartJob - Follow Service")
        async {
            val movieJob = jobParameters?.extras?.getInt(JobNotification.BUNDLE_ID)?.let {
                bg {try {App.moviesProvider.movieDetail(it)}catch (e: Throwable){null}}
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //todo create Notification Channel
                Notification.Builder(this@FollowNotificationService, "")
            }else{
                Notification.Builder(this@FollowNotificationService)
            }.apply {
                movieJob?.await()?.run {
                    setContentText(this.title)
                    if (poster_path != null && poster_path.isNotEmpty()) {
                        try{App.moviesProvider.image(poster_path, Options.poster_sizes[Options.SMALL]!!, {setLargeIcon(it)}) }catch (e:Throwable){null}
                    }
                }

                setContentTitle("New movie released")
                setSmallIcon(R.drawable.ic_video_camera_80s)
                //setLargeIcon(BitmapFactory.decodeResource(App.instance.resources, R.drawable.ic_video_camera))

                //todo set movie image as icon
                //setContentIntent(pIntent)
                setAutoCancel(false)

                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(123, build())
            }
        }

        return true //code is running asynchronously
    }

}