package com.filip.movieexplorer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey
    val imdbId: String,
    val title: String,
    val year: String,
    val director: String,
    val genre: String,
    val posterUrl: String
)


