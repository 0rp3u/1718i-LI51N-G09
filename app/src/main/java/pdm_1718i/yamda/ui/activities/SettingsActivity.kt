package pdm_1718i.yamda.ui.activities

import android.os.Bundle
import pdm_1718i.yamda.R

class SettingsActivity : BaseActivity(withMenu = false) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }
}
