package pdm_1718i.yamda.data.utils

import android.content.SharedPreferences
import android.content.res.Resources
import android.preference.PreferenceManager
import pdm_1718i.yamda.R
import pdm_1718i.yamda.ui.App

object UtilPreferences {

    val DEFAULT_INT_VALUE = 0
    val DEFAULT_BOOLEAN_VALUE = false

    val prefs : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.instance)
    val resources : Resources = App.instance.resources
    val editor: SharedPreferences.Editor = prefs.edit()



    fun getWifi(): Boolean{
        return prefs
                .getBoolean(resources.getString(R.string.preference_connection_type_key),
                            resources.getString(R.string.preference_connection_type_DEFAULT_VALUE).toBoolean()
                )
    }

    fun getPeriodicity() : Int{
        return prefs
                .getInt(resources.getString(R.string.preference_periodicity_key),
                        resources.getString(R.string.preference_periodicity_DEFAULT_VALUE).toInt())
    }

    fun getLowBatteryUpdate(): Boolean{
        return prefs
                .getBoolean(resources.getString(R.string.preference_low_battery_update_key),
                        resources.getString(R.string.preference_low_battery_update_DEFAULT_VALUE).toBoolean())
    }

    fun getNotification(): Boolean{
        return prefs
                .getBoolean(resources.getString(R.string.preference_notification_key),
                        resources.getString(R.string.preference_notification_DEFAULT_VALUE).toBoolean())
    }

    fun getVibration(): Boolean{
        return prefs
                .getBoolean(resources.getString(R.string.preference_vibrate_key),
                        resources.getString(R.string.preference_vibrate_DEFAULT_VALUE).toBoolean())
    }

    fun putBooleanPreference(key:String, value:Boolean)
    {
        editor.putBoolean(key, value)
        editor.commit()
    }
}