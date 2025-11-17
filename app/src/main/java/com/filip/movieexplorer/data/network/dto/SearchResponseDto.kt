package com.filip.movieexplorer.data.network.dto

import com.squareup.moshi.Json

/**
 * DTO for OMDb API search response.
 */
data class SearchResponseDto(
    @Json(name = "Response")
    val response: String,
    @Json(name = "Search")
    val search: List<MovieSearchItemDto>?,
    @Json(name = "totalResults")
    val totalResults: String?,
    @Json(name = "Error")
    val error: String?
)

/**
 * DTO for individual movie item in search results.
 */
data class MovieSearchItemDto(
    @Json(name = "Title")
    val title: String,
    @Json(name = "Year")
    val year: String,
    @Json(name = "imdbID")
    val imdbId: String,
    @Json(name = "Type")
    val type: String,
    @Json(name = "Poster")
    val poster: String
)

