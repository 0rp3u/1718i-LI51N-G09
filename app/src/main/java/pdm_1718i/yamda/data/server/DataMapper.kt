package pdm_1718i.yamda.data.server

import com.example.pdm_1718i.yamda.data.server.MovieDetailResult
import com.example.pdm_1718i.yamda.data.server.MovieSearchResult
import pdm_1718i.yamda.extensions.getCalendar
import pdm_1718i.yamda.model.DetailedMovie
import pdm_1718i.yamda.model.Genre
import pdm_1718i.yamda.model.Movie
import pdm_1718i.yamda.extensions.removePref

class DataMapper {

    fun mapToMovieList(result: MovieSearchResult?): List<Movie> {
       if(result == null ) return listOf()
       return result.results.map { Movie(String.removePref(it.poster_path, 1), getCalendar(it.release_date), it.id, it.title, String.removePref(it.backdrop_path, 1) , it.vote_average ) }
    }

    fun mapToMovieDetail(result: MovieDetailResult): DetailedMovie {
        return  DetailedMovie(
                String.removePref(result.poster_path, 1),
                String.removePref(result.backdrop_path, 1),
                getCalendar(result.release_date),
                result.id,
                result.title,
                result.vote_average.toFloat(),
                result.budget,
                result.genres.map { Genre(it.name) },
                result.overview
        )
    }
}
