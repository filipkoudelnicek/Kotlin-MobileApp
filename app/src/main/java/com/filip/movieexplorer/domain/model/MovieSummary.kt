package com.filip.movieexplorer.domain.model

/**
 * Domain representation of a movie item returned by OMDb search.
 */
data class MovieSummary(
    val imdbId: String,
    val title: String,
    val year: String,
    val type: String,
    val posterUrl: String
)


