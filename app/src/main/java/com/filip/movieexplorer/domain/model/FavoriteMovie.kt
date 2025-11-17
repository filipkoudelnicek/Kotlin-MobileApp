package com.filip.movieexplorer.domain.model

data class FavoriteMovie(
    val imdbId: String,
    val title: String,
    val year: String,
    val director: String,
    val genre: String,
    val posterUrl: String
)


