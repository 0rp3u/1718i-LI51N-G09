package pdm_1718i.yamda.data.server

import com.example.pdm_1718i.yamda.data.server.MovieDetailResult
import com.example.pdm_1718i.yamda.data.server.MovieSearchResult
import pdm_1718i.yamda.model.DetailedMovie
import pdm_1718i.yamda.model.Genre
import pdm_1718i.yamda.model.Movie

class DataMapper {
/*
fun mapToMovie(jsonMovie : JSONObject) : Movie {


}
*/

    fun mapToMovieList(result: MovieSearchResult?): List<Movie> {

       if(result == null ) return listOf()
       return result.results.map { Movie(it.poster_path?.substring(1)?: "",it.release_date, it.id, it.title,it.backdrop_path?.substring(1)?:"" , it.vote_average ) }
    }

    fun mapToMovieDetail(result: MovieDetailResult): DetailedMovie {


        return  DetailedMovie(
                result.poster_path,
                result.backdrop_path,
                result.release_date,
                result.id,
                result.title,
                result.vote_average.toFloat(),
                result.budget,
                result.genres.map {
                    Genre(it.name)
                },
                result.overview)
    }
}