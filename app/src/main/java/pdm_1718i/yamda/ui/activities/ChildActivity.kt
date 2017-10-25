package pdm_1718i.yamda.ui.activities

import android.support.v7.app.AppCompatActivity

/**
 * Created by Red on 25/10/2017.
 */
class ChildActivity : AppCompatActivity() {

    init{
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}