package pdm_1718i.yamda.ui.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import pdm_1718i.yamda.R
import pdm_1718i.yamda.data.server.Options
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.ui.App
import pdm_1718i.yamda.ui.adapters.MainActAdapter

class MainActivity : BaseActivity() {
    private val REQUEST_TYPE = "requestType"

    private val DEFAULT_PAGINATION: Int = 1
    private val DEFAULT_ITEM_NUMBER: Int = 4

    //private val POPULAR_ITEM_LIST: IntArray =
    //        intArrayOf(R.id.popular_image_1, R.id.popular_image_2, R.id.popular_image_3, R.id.popular_image_4)
    private val UPCOMING_ITEM_LIST: IntArray =
            intArrayOf(R.id.upcoming_image_1, R.id.upcoming_image_2, R.id.upcoming_image_3, R.id.upcoming_image_4)
    private val PLAYING_ITEM_LIST: IntArray =
            intArrayOf(R.id.theaters_image_1, R.id.theaters_image_2, R.id.theaters_image_3, R.id.theaters_image_4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
/*
        App.moviesProvider.nowPlayingMovies(DEFAULT_PAGINATION, {
            getImageAndSet(it, DEFAULT_ITEM_NUMBER, POPULAR_ITEM_LIST)
        })

        App.moviesProvider.upcomingMovies(DEFAULT_PAGINATION,{
            getImageAndSet(it, DEFAULT_ITEM_NUMBER, UPCOMING_ITEM_LIST)
        })

        App.moviesProvider.popularMovies(DEFAULT_PAGINATION,{
            getImageAndSet(it, DEFAULT_ITEM_NUMBER, PLAYING_ITEM_LIST)
        })
*/

        val recyclerViewNowPlaying = findViewById(R.id.recycler_view_nowplaying) as RecyclerView
        recyclerViewNowPlaying.layoutManager = LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)
        App.moviesProvider.nowPlayingMovies(DEFAULT_PAGINATION,{
            val adapter = MainActAdapter(it)
            recyclerViewNowPlaying.adapter = adapter
        })

        val recyclerViewUpcoming = findViewById(R.id.recycler_view_upcoming) as RecyclerView
        recyclerViewUpcoming.layoutManager = LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)
        App.moviesProvider.upcomingMovies(DEFAULT_PAGINATION,{
            val adapter = MainActAdapter(it)
            recyclerViewUpcoming.adapter = adapter
        })

        val recyclerViewPopular = findViewById(R.id.recycler_view_popular) as RecyclerView
        recyclerViewPopular.layoutManager = LinearLayoutManager(this, LinearLayout.HORIZONTAL, false)
        App.moviesProvider.popularMovies(DEFAULT_PAGINATION,{
            val adapter = MainActAdapter(it)
            recyclerViewPopular.adapter = adapter
        })







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
