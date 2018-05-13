package pdm_1718i.yamda.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import pdm_1718i.yamda.R

class AboutActivity :BaseActivity(menuOptions = false) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }

    fun onLogoClick(view: View){
        startActivity(Intent (Intent.ACTION_VIEW, Uri.parse(getString(R.string.TMDB_HOMEPAGE_URL))))
    }
}
