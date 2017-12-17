package pdm_1718i.yamda.ui.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.db.Util
import pdm_1718i.yamda.data.server.Options
import pdm_1718i.yamda.data.server.Options.BIG
import pdm_1718i.yamda.extensions.getDateFromCalendar
import pdm_1718i.yamda.extensions.getYearFromCalendar
import pdm_1718i.yamda.extensions.isFuture
import pdm_1718i.yamda.extensions.toast
import pdm_1718i.yamda.model.MovieDetail
import pdm_1718i.yamda.ui.App

class MovieDetailActivity : BaseActivity() {

    private val release_date            by lazy { findViewById(R.id.release_date)   as TextView }
    private val ratingTextView          by lazy { findViewById(R.id.rating)         as TextView }
    private val genreTextView           by lazy { findViewById(R.id.genres)         as TextView }
    private val overviewTextView        by lazy { findViewById(R.id.overview)       as TextView }
    private val moviePoster             by lazy { findViewById(R.id.movie_poster)   as ImageView }
    private val followIcon              by lazy { findViewById(R.id.follow_icon)    as ImageView }
//    private val NOTIFICATION_OFF_ICON   by lazy { getDrawable(R.drawable.ic_notifications_none_black_24dp) }
//    private val NOTIFICATION_ON_ICON    by lazy{ getDrawable(R.drawable.ic_notifications_active_black_24dp) }
    private val NOTIFICATION_OFF_ICON = R.drawable.ic_notifications_none_black_24dp
    private val NOTIFICATION_ON_ICON = R.drawable.ic_notifications_active_black_24dp

    private var followIconState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        val movieId = intent.getIntExtra("movieId", -1)
        async(UI) {updateUI( bg{App.moviesProvider.movieDetail(movieId)}.await()) }
    }

    //@RequiresApi(api = 21)//getDrawable
    private fun updateUI(movieDetail: MovieDetail) {
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
            if(movieDetail.release_date != null && movieDetail.release_date.isFuture()){
                if(movieDetail.isFollowing){
                    toast("Thread:${Thread.currentThread()} ON")
                    followIcon.setImageResource(NOTIFICATION_ON_ICON)
                    followIconState = true
                }else{
                    toast("Thread:${Thread.currentThread()} OFF")
                    followIcon.setImageResource(NOTIFICATION_OFF_ICON)
                    followIconState = false
                }
                registerEvents(movieDetail)
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