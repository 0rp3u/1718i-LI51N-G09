package pdm_1718i.yamda.ui.activities

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.db.MovieContract
import pdm_1718i.yamda.data.server.Options
import pdm_1718i.yamda.data.server.Options.Companion.BIG
import pdm_1718i.yamda.extensions.getDateFromCalendar
import pdm_1718i.yamda.extensions.getYearFromCalendar
import pdm_1718i.yamda.model.DetailedMovie
import pdm_1718i.yamda.ui.App

class MovieDetailActivity : BaseActivity() {

    private val release_date        by lazy { findViewById(R.id.release_date)   as TextView }
    private val ratingTextView      by lazy { findViewById(R.id.rating)         as TextView }
    private val genreTextView       by lazy { findViewById(R.id.genres)         as TextView }
    private val overviewTextView    by lazy { findViewById(R.id.overview)       as TextView }
    private val moviePoster         by lazy { findViewById(R.id.movie_poster)   as ImageView }
    private val followIcon          by lazy { findViewById(R.id.follow_icon)   as ImageView }

    private var followIconState: Boolean = false
    private val NOTIFICATION_OFF = R.drawable.ic_notifications_none_black_24dp
    private val NOTIFICATION_ON = R.drawable.ic_notifications_active_black_24dp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        val movieId = intent.getIntExtra("movieId", -1)
        registerEvents(movieId)
        App.moviesProvider.movieDetail(movieId, { updateUI(it) })
    }

    private fun updateUI(movieDetail: DetailedMovie) {
        with(movieDetail){
            this@MovieDetailActivity.title = "$title"
            this@MovieDetailActivity.supportActionBar?.subtitle = "${getYearFromCalendar(release_date)}"
            this@MovieDetailActivity.release_date.text = getDateFromCalendar(release_date)
            ratingTextView.text = vote_average.toString()
            val genres = genres.joinToString(separator = ", ") {it.name}
            genreTextView.text = genres
            overviewTextView.text = overview

            if (poster_path != null && poster_path.isNotEmpty()) {
                App.moviesProvider.image(poster_path, moviePoster, Options.poster_sizes[BIG]!!)
            }else{
                moviePoster.setImageResource(R.drawable.ic_movie_thumbnail)
            }

            if(movieDetail.isFollowing){
                followIcon.setImageResource(NOTIFICATION_ON)
                followIconState = true
            }else{
                followIcon.setImageResource(NOTIFICATION_OFF)
                followIconState = false
            }
        }
    }

    private fun registerEvents(movieID: Int){
        followIcon.setOnClickListener {
            val MOVIE_ID = Integer.toString(movieID)
            val cr = this.contentResolver
            val uri = MovieContract.MovieDetails.CONTENT_URI
            val movieUri = Uri.withAppendedPath(uri, MOVIE_ID)
            val contentValues = ContentValues().apply {
                put(MovieContract.MovieDetails.IS_FOLLOWING, !followIconState)
            }
            val where = "_ID = ?"
            val selectionArgs: Array<String> = arrayOf(MOVIE_ID)
            val nchanged = cr.update(movieUri, contentValues, where, selectionArgs)
            val nchanged1 = cr.insert(movieUri, )
            with(it as ImageView){
                if(followIconState){
                    setImageResource(NOTIFICATION_OFF)
                    followIconState = false
                }else{
                    setImageResource(NOTIFICATION_ON)
                    followIconState = true
                }
            }
        }
    }

}