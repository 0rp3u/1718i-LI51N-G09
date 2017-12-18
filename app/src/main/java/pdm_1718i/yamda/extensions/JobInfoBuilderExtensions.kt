package pdm_1718i.yamda.extensions

import android.app.job.JobInfo
import pdm_1718i.yamda.data.utils.UtilPreferences

fun JobInfo.Builder.getNetworkTypeFromPreferences(): Int = when(UtilPreferences.getOnlyWifi()){
    true -> JobInfo.NETWORK_TYPE_UNMETERED
    false -> JobInfo.NETWORK_TYPE_ANY
}