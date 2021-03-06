package pdm_1718i.yamda.ui.activities

import android.os.Bundle
import android.preference.PreferenceActivity
import android.preference.PreferenceManager
import pdm_1718i.yamda.R
import pdm_1718i.yamda.ui.fragments.SettingsFragment

/**
 * A [PreferenceActivity] that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 *
 * See [Android Design: Settings](http://developer.android.com/design/patterns/settings.html)
 * for design guidelines and the [Settings API Guide](http://developer.android.com/guide/topics/ui/settings.html)
 * for more information on developing a Settings UI.
 */

class SettingsActivity : BaseActivity(menuOptions = false){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Preferences" //meter numa constante
        fragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment())
                .commit()

        /*
            Context, xml das preferences, ReadAgain
            Read Again
                Whether to re-read the default values.
                If false, this method sets the default values only if this method has
                never been called in the past (or if the KEY_HAS_SET_DEFAULT_VALUES in
                the default value shared preferences file is false).
                To attempt to set the default values again bypassing this check,
                set readAgain to true.
         */
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }
}
