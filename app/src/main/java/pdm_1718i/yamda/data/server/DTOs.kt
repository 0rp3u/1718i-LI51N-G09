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
        val vote_average : Number,
        val total_results : Int,
        val total_pages : Int
)

data class MovieSearchResult(val page: Int, val results: List<Movie>)