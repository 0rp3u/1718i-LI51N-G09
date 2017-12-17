package pdm_1718i.yamda.ui.utils

import android.content.SharedPreferences
import android.content.res.Resources
import android.preference.PreferenceManager
import pdm_1718i.yamda.R
import pdm_1718i.yamda.ui.App

object UtilPreferences {

    val DEFAULT_INT_VALUE = 0
    val DEFAULT_BOOLEAN_VALUE = false

    fun getWifi(resources: Resources, prefs : SharedPreferences): Boolean{
        return prefs.getBoolean(resources.getString(R.string.connection_type_key), DEFAULT_BOOLEAN_VALUE)
    }

    fun getPeriodicity() : Int{
        return PreferenceManager.getDefaultSharedPreferences(App.instance)
                .getInt(App.instance.resources.getString(R.string.preference_periodicity_key), DEFAULT_INT_VALUE)
    }

    fun getNotification(resources: Resources, prefs : SharedPreferences): Boolean{
        return prefs.getBoolean(resources.getString(R.string.preference_notification_key), DEFAULT_BOOLEAN_VALUE)
    }

    fun getVibration(resources: Resources, prefs : SharedPreferences): Boolean{
        return prefs.getBoolean(resources.getString(R.string.preference_vibrate_key), DEFAULT_BOOLEAN_VALUE)
    }
}