package com.filip.movieexplorer.data.network.dto

import com.squareup.moshi.Json

/**
 * DTO for OMDb API movie detail response.
 */
data class MovieDetailDto(
    @Json(name = "Response")
    val response: String,
    @Json(name = "Title")
    val title: String?,
    @Json(name = "Year")
    val year: String?,
    @Json(name = "Director")
    val director: String?,
    @Json(name = "Genre")
    val genre: String?,
    @Json(name = "Plot")
    val plot: String?,
    @Json(name = "Poster")
    val poster: String?,
    @Json(name = "imdbID")
    val imdbId: String?,
    @Json(name = "Actors")
    val actors: String?,
    @Json(name = "imdbRating")
    val imdbRating: String?,
    @Json(name = "Error")
    val error: String?
)

