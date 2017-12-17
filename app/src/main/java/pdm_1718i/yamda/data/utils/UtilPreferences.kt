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
                .getBoolean(resources.getString(R.string.connection_type_key), DEFAULT_BOOLEAN_VALUE)
    }

    fun getPeriodicity() : Int{
        return prefs
                .getInt(resources.getString(R.string.preference_periodicity_key), DEFAULT_INT_VALUE)
    }

    fun getNotification(): Boolean{
        return prefs
                .getBoolean(resources.getString(R.string.preference_notification_key), DEFAULT_BOOLEAN_VALUE)
    }

    fun getVibration(): Boolean{
        return prefs
                .getBoolean(resources.getString(R.string.preference_vibrate_key), DEFAULT_BOOLEAN_VALUE)
    }

    fun putBooleanPreference(key:String, value:Boolean)
    {
        editor.putBoolean(key, value)
        editor.commit()
    }
}