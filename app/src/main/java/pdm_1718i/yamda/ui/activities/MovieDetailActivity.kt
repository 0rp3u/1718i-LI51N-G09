package pdm_1718i.yamda.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.db.Util
import pdm_1718i.yamda.data.server.ImageOption
import pdm_1718i.yamda.extensions.*
import pdm_1718i.yamda.model.MovieDetail
import pdm_1718i.yamda.ui.App

class MovieDetailActivity : BaseActivity() {

    private val releaseDateView         by lazy { findViewById<TextView>(R.id.release_date) }
    private val ratingTextView          by lazy { findViewById<TextView>(R.id.rating) }
    private val genreTextView           by lazy { findViewById<TextView>(R.id.genres) }
    private val overviewTextView        by lazy { findViewById<TextView>(R.id.overview) }
    private val moviePoster             by lazy { findViewById<ImageView>(R.id.movie_poster) }
    private val followIcon              by lazy { findViewById<ImageView>(R.id.follow_icon) }
    private val imdbIcon                by lazy { findViewById<ImageView>(R.id.imdb_button) }

    companion object {
        val BUNDLE_ID_KEY = "movieId"
        val BUNDLE_INVALID_VALUE = -1
    }

    private val NOTIFICATION_OFF_ICON = R.drawable.ic_notifications_none_black_24dp
    private val NOTIFICATION_ON_ICON = R.drawable.ic_notifications_active_black_24dp

    private var followIconState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        val movieId = intent.getIntExtra(BUNDLE_ID_KEY, BUNDLE_INVALID_VALUE)
        async(UI) {updateUI( bg{App.moviesProvider.movieDetail(movieId)}.await()) }
    }

    private fun updateUI(movieDetail: MovieDetail) {
        with(movieDetail){
            this@MovieDetailActivity.title = title
            this@MovieDetailActivity.supportActionBar?.subtitle = getYearFromCalendar(release_date)
            this@MovieDetailActivity.releaseDateView.text = getDateFromCalendar(release_date)
            ratingTextView.text = vote_average.asRounded()
            val genres = genres.joinToString(separator = ", ") {it.name}
            genreTextView.text = genres
            overviewTextView.text = overview
            if (poster_path.isNotEmpty())
                App.moviesProvider.image(poster_path, moviePoster, ImageOption.BIG)
            else
                moviePoster.setImageResource(R.drawable.ic_movie_thumbnail)

            if(movieDetail.release_date != null && movieDetail.release_date.isFuture()){
                followIconState = if(movieDetail.isFollowing){
                    followIcon.setImageResource(NOTIFICATION_ON_ICON)
                    true
                }else{
                    followIcon.setImageResource(NOTIFICATION_OFF_ICON)
                    false
                }
                registerEvents(movieDetail)
            }

            if(movieDetail.imdbId.isNotEmpty()){
                imdbIcon.setOnClickListener { startActivity(Intent (Intent.ACTION_VIEW, Uri.parse(getString(R.string.IMDB_TITLE_URL).plus(movieDetail.imdbId)))) }
            }
        }
    }

    private fun registerEvents(movie: MovieDetail){
        followIcon.setOnClickListener {
            it.isEnabled = false
            async(UI) {
                val task: Deferred<Boolean?> = bg {
                   Util.updateFollowingState(movie, !followIconState)
                }
                val result: Boolean? = task.await()
                if(result != null){
                    with(it as ImageView){
                        if(result){
                            toast(App.instance.getString(R.string.toast_follow))
                            setImageResource(NOTIFICATION_ON_ICON)
                        }else{
                            toast(App.instance.getString(R.string.toast_unfollow))
                            setImageResource(NOTIFICATION_OFF_ICON)
                        }
                        followIconState = result
                    }
                }
                it.isEnabled = true
            }
        }
    }

}