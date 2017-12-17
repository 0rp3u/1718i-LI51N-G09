package pdm_1718i.yamda.ui.utils

import android.content.SharedPreferences
import android.content.res.Resources
import pdm_1718i.yamda.R

class UtilPreferences {

    fun getWifi(resources: Resources, prefs : SharedPreferences): Boolean{
        return prefs.getBoolean(resources.getString(R.string.connection_type_key), false)
    }

    fun getPeriodicity(resources: Resources, prefs : SharedPreferences) : Int{
        return prefs.getInt(resources.getString(R.string.preference_periodicity_key), 0)
    }

    fun getNotification(resources: Resources, prefs : SharedPreferences): Boolean{
        return prefs.getBoolean(resources.getString(R.string.preference_notification_key), false)
    }

    fun getVibration(resources: Resources, prefs : SharedPreferences): Boolean{
        return prefs.getBoolean(resources.getString(R.string.preference_vibrate_key), false)
    }
}