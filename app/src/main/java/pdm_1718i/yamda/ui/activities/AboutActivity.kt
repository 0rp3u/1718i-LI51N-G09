package pdm_1718i.yamda.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import pdm_1718i.yamda.R

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun onLogoClick(view: View){
        val intent = Intent (Intent.ACTION_VIEW, Uri.parse(getString(R.string.TMDB_HOMEPAGE_URL)))
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
