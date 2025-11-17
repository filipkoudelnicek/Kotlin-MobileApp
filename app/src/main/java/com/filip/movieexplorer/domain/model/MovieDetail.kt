package com.filip.movieexplorer.domain.model

/**
 * Domain representation of detailed movie info from OMDb.
 */
data class MovieDetail(
    val imdbId: String,
    val title: String,
    val year: String,
    val director: String,
    val genre: String,
    val plot: String,
    val posterUrl: String,
    val actors: String,
    val imdbRating: String
)


