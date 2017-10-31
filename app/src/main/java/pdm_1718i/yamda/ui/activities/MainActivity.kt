package pdm_1718i.yamda.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.server.Options
import pdm_1718i.yamda.data.server.TMDBService.Companion.DEFAULT_PAGINATION
import pdm_1718i.yamda.extensions.NO_INTERNET_CONNECTION
import pdm_1718i.yamda.extensions.caseTrue
import pdm_1718i.yamda.extensions.runIf
import pdm_1718i.yamda.extensions.toast
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App
import pdm_1718i.yamda.ui.App.Companion.isNetworkAvailable

class MainActivity : BaseActivity() {
    private val REQUEST_TYPE = "requestType"

    private val DEFAULT_ITEM_NUMBER: Int = 4
    private val POPULAR_ITEM_LIST: IntArray =
            intArrayOf(R.id.popular_image_1, R.id.popular_image_2, R.id.popular_image_3, R.id.popular_image_4)
    private val UPCOMING_ITEM_LIST: IntArray =
            intArrayOf(R.id.upcoming_image_1, R.id.upcoming_image_2, R.id.upcoming_image_3, R.id.upcoming_image_4)
    private val PLAYING_ITEM_LIST: IntArray =
            intArrayOf(R.id.theaters_image_1, R.id.theaters_image_2, R.id.theaters_image_3, R.id.theaters_image_4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        runIf(isNetworkAvailable){
            App.moviesProvider.popularMovies(DEFAULT_PAGINATION, {
                getImageAndSet(it, DEFAULT_ITEM_NUMBER, POPULAR_ITEM_LIST)
            })

            App.moviesProvider.upcomingMovies(DEFAULT_PAGINATION,{
                getImageAndSet(it, DEFAULT_ITEM_NUMBER, UPCOMING_ITEM_LIST)
            })

            App.moviesProvider.nowPlayingMovies(DEFAULT_PAGINATION,{
                getImageAndSet(it, DEFAULT_ITEM_NUMBER, PLAYING_ITEM_LIST)
            })
        }.not().caseTrue{ toast(NO_INTERNET_CONNECTION) }

    }

    fun onPopularMore(view: View){
        with(Intent(applicationContext, MovieListActivity::class.java)){
            putExtra(REQUEST_TYPE, "Popular")
            startActivity(this)
        }
    }

    fun onInTheatersMore(view: View){
        with(Intent(applicationContext, MovieListActivity::class.java)){
            putExtra(REQUEST_TYPE, "Now Playing")
            startActivity(this)
        }
    }

    fun onUpcomingMore(view: View){
        with(Intent(applicationContext, MovieListActivity::class.java)){
            putExtra(REQUEST_TYPE, "Upcoming")
            startActivity(this)
        }
    }

   private fun getImageAndSet(it: List<Movie>, take: Int, image_views: IntArray){
        it.take(take).forEachIndexed { index, movie ->
            with(findViewById(image_views[index]) as ImageView){
                App.moviesProvider.image(movie.poster_path, this, Options.poster_sizes["SMALL"]!!)
                setOnClickListener({
                    startActivity(Intent(applicationContext, MovieDetailActivity::class.java).putExtra("movieId",movie.id ))
                })
            }
        }
    }


}
