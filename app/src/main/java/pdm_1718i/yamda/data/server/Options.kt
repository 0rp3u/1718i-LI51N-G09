package pdm_1718i.yamda.data.server;


object Options{
    val SMALL = "SMALL"
    val BIG = "BIG"
    val ORIGIN = "original"
    val poster_sizes: HashMap<String, String> = hashMapOf(SMALL to "w185", BIG to "w500", ORIGIN to "original")
}
