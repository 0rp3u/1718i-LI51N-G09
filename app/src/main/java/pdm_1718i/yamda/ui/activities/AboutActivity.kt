package pdm_1718i.yamda.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.Secure.getString
import android.support.v7.app.AppCompatActivity
import android.view.View
import pdm_1718i.yamda.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }

    fun onLogoClick(view: View){
        val intent = Intent (Intent.ACTION_VIEW, Uri.parse(getString(R.string.TMDB_URL)))
        startActivity(intent)

    }
}
