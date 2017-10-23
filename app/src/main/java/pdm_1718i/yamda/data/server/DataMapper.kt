package pdm_1718i.yamda.data.server

import com.example.pdm_1718i.yamda.data.server.MovieSearchResult
import org.json.JSONArray
import org.json.JSONObject
import pdm_1718i.yamda.extensions.asSequence
import pdm_1718i.yamda.model.Movie

class DataMapper {
/*
fun mapToMovie(jsonMovie : JSONObject) : Movie {


}
*/

    fun mapToMovieList(result: MovieSearchResult?): List<Movie> {

       if(result == null ) return listOf()

       return result.results.map { Movie(it.poster_path?: "",it.release_date, it.id, it.title,it.backdrop_path?:"" ,it.vote_average ) }
    }

}