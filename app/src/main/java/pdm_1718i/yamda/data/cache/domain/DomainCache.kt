package pdm_1718i.yamda.data.cache.domain

import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import pdm_1718i.yamda.data.MoviesDataSource
import pdm_1718i.yamda.model.DetailedMovie
import pdm_1718i.yamda.model.Movie

/*
class DomainCache(private val provider : MoviesDataSource): MoviesDataSource{
    private val cache : MutableMap<String, Any> = mutableMapOf()  //possible need to be thread safe??

    override fun popularMovies(page: Int, completionHandler: (movies: List<Movie>) -> Unit){
        val uri = "popularMovies/page/$page"
        if(cache.contains(uri))completionHandler(cache[uri] as List<Movie>)
        else provider.popularMovies(page, {
               cache.put(uri, it)
               completionHandler(it)
           })
    }

    override fun upcomingMovies(page: Int, completionHandler: (movies: List<Movie>) -> Unit) {
        val uri = "upcomingMovies/page/$page"
        if(cache.contains(uri))completionHandler(cache[uri] as List<Movie>)
        else provider.upcomingMovies(page, {
            cache.put(uri, it)
            completionHandler(it)
        })
    }

    override fun playingMovies(page: Int, completionHandler: (movies: List<Movie>) -> Unit) {
        val uri = "playingMovies/page/$page"
        if(cache.contains(uri))completionHandler(cache[uri] as List<Movie>)
        else provider.playingMovies(page, {
            cache.put(uri, it)
            completionHandler(it)
        })
    }

    override fun movieSearch(query: String, page: Int, completionHandler: (movies: List<Movie>) -> Unit) {
        val uri = "movieSearch/query/$query/page/$page"
        if(cache.contains(uri)){
            Log.d("cache", "$uri was cached!")
            completionHandler(cache[uri] as List<Movie>)
        }
        else provider.movieSearch(query,page, {
            cache.put(uri, it)
            completionHandler(it)
        })
    }

    override fun movieDetail(id: Int, completionHandler: (movies: DetailedMovie) -> Unit) {
        val uri = "movieDetail/id/$id"
        if(cache.contains(uri))
            completionHandler(cache[uri] as DetailedMovie)
        else provider.movieDetail(id, {
            cache.put(uri, it)
            completionHandler(it)
        })
    }

    override fun movieImage(image_id: String, imageView: ImageView, image_size: String) {
        provider.movieImage(image_id, imageView, image_size)
    }

    override fun movieImage(image_id: String, bitmapCompletionHandler: (bitmap: Bitmap)-> Unit, image_size: String) {
        provider.movieImage(image_id, bitmapCompletionHandler, image_size)
    }

}
*/