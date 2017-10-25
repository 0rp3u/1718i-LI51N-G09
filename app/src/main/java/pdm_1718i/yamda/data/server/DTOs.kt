package com.example.pdm_1718i.yamda.data.server

/**
 * Created by orpheu on 10/19/17.
 */

data class Movie(
        val poster_path :String?,
        val adult : Boolean,
        val overview : String,
        val release_date :String,
        val genre_ids : Array<Int>,
        val id : Int,
        val original_title : String,
        val original_language : String,
        val title : String,
        val backdrop_path : String?,
        val popularity : Number,
        val vote_count : Number,
        val video : Boolean,
        val vote_average : Double,
        val total_results : Int,
        val total_pages : Int
)

data class MovieSearchResult(val page: Int, val results: List<Movie>)

data class MovieDetailResult(
        val adult: Boolean,
        val backdrop_path : String?,
        val budget :Int,
        val genres :List<Genre>,
        val name :String,
        val homepage : String?,
        val id : Int,
        val imdb_id : String?,
        val original_language :String,
        val original_title :String,
        val overview : String?,
        val popularity : Number,
        val poster_path :String?,
        //val production_companies array[object]
        //val production_countries : array[object]
        val release_date: String,
        val revenue : Int,
        val runtime : Int?,
        //val spoken_languages array[object]
        val status : String,
        val tagline : String?,
        val title :String,
        val video : Boolean,
        val vote_average :Number,
        val vote_count : Int
)

data class Genre(
        val id : Int,
        val name: String
)