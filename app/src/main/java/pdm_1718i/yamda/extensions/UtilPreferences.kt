package pdm_1718i.yamda.extensions

import android.content.SharedPreferences
import android.content.res.Resources
import pdm_1718i.yamda.R

class UtilPreferences {

    val DEFAULT_INT_VALUE = 0
    val DEFAULT_BOOLEAN_VALUE = false

    fun getWifi(resources: Resources, prefs : SharedPreferences): Boolean{
        return prefs.getBoolean(resources.getString(R.string.connection_type_key), DEFAULT_BOOLEAN_VALUE)
    }

    fun getPeriodicity(resources: Resources, prefs : SharedPreferences) : Int{
        return prefs.getInt(resources.getString(R.string.preference_periodicity_key), DEFAULT_INT_VALUE)
    }

    fun getNotification(resources: Resources, prefs : SharedPreferences): Boolean{
        return prefs.getBoolean(resources.getString(R.string.preference_notification_key), DEFAULT_BOOLEAN_VALUE)
    }

    fun getVibration(resources: Resources, prefs : SharedPreferences): Boolean{
        return prefs.getBoolean(resources.getString(R.string.preference_vibrate_key), DEFAULT_BOOLEAN_VALUE)
    }
}