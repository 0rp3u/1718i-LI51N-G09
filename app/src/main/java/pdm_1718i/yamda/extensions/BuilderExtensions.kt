package pdm_1718i.yamda.extensions

import android.app.Notification
import android.app.job.JobInfo
import pdm_1718i.yamda.data.utils.UtilPreferences

fun JobInfo.Builder.getNetworkTypeFromPreferences(): Int = when(UtilPreferences.getOnlyWifi()){
    true -> JobInfo.NETWORK_TYPE_UNMETERED
    false -> JobInfo.NETWORK_TYPE_ANY
}

fun Notification.Builder.getVibrationFromPreferences(): Int = when(UtilPreferences.getVibration()){
    true -> Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND
//    true -> Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND
    false -> Notification.DEFAULT_ALL
}