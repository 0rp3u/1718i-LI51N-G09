package pdm_1718i.yamda.data.utils

import android.content.SharedPreferences
import android.content.res.Resources
import android.preference.PreferenceManager
import pdm_1718i.yamda.R
import pdm_1718i.yamda.ui.App

object UtilPreferences {

    val prefs : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.instance)
    val resources : Resources = App.instance.resources
    val editor: SharedPreferences.Editor = prefs.edit()

    fun getOnlyWifi(): Boolean{
        return prefs
                .getBoolean(resources.getString(R.string.preference_connection_type_key),
                            resources.getBoolean(R.bool.preference_connection_type_DEFAULT_VALUE)
                )
    }

    fun getPeriodicity() : Int{
        val DEFAULT_VALUE : Int = resources.getInteger(R.integer.preference_periodicity_DEFAULT_VALUE)
        val key : String= resources.getString(R.string.preference_periodicity_key)
        val ret : String =prefs
                .getString(key,
                        DEFAULT_VALUE.toString())
        return ret.toInt()
    }

    fun getLowBatteryUpdate(): Boolean{
        return prefs
                .getBoolean(resources.getString(R.string.preference_low_battery_update_key),
                        resources.getBoolean(R.bool.preference_low_battery_update_DEFAULT_VALUE))
    }

    fun getNotification(): Boolean{
        return prefs
                .getBoolean(resources.getString(R.string.preference_notification_key),
                        resources.getBoolean(R.bool.preference_notification_DEFAULT_VALUE))
    }

    fun getVibration(): Boolean{
        return prefs
                .getBoolean(resources.getString(R.string.preference_vibrate_key),
                        resources.getBoolean(R.bool.preference_vibrate_DEFAULT_VALUE))
    }

    fun putBooleanPreference(key:String, value:Boolean)
    {
        editor.putBoolean(key, value)
        editor.commit()
    }
}