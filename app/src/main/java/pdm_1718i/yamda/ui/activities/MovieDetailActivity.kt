package pdm_1718i.yamda.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import pdm_1718i.yamda.R

class MovieDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        Toast.makeText(this, "got id: ${intent.getIntExtra("movieId", -1)}", Toast.LENGTH_SHORT).show()
    }
}
