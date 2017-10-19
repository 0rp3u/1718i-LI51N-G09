package pdm_1718i.yamda.data.server

import org.json.JSONArray
import org.json.JSONObject
import pdm_1718i.yamda.extensions.asSequence
import pdm_1718i.yamda.model.Movie

class DataMapper {
/*
fun mapToMovie(jsonMovie : JSONObject) : Movie {


}
*/

    fun mapToMovieList(jsonMovies: JSONObject?): List<Movie> {


        val jsonTeachers = jsonMovies?.get("results") as JSONArray

        return jsonTeachers
                .asSequence()
                .map {
                    Movie(
                            it["poster_path"] as String,
                            it["adult"] as Boolean,
                            it["overview"] as String,
                            it["release_date"] as String,
                            HashSet(),
                            it["id"] as Int,
                            it["original_title"] as String,
                            it["original_language"] as String,
                            it["title"] as String,
                            "",
                            it["popularity"] as Double,
                            it["vote_count"] as Int,
                            it["vote_average"] as Number
                    )
                }
                .toList()
    }
}