package pdm_1718i.yamda.data.server;

/**
 * Created by Red on 25/10/2017.
 */

class Options {
    companion object{
        val base_url = "http://image.tmdb.org/t/p/"
        val secure_base_url = "https://image.tmdb.org/t/p/"
        val poster_sizes: HashMap<String, String> = hashMapOf("SMALL" to "w185", "BIG" to "w500")
    }
}
