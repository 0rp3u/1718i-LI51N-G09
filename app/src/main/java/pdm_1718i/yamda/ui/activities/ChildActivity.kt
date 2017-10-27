package pdm_1718i.yamda.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by Red on 25/10/2017.
 */
open class ChildActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}