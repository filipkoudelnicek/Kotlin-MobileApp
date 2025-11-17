package com.filip.movieexplorer.data.mapper

import com.filip.movieexplorer.data.local.entity.FavoriteMovieEntity
import com.filip.movieexplorer.data.network.dto.MovieDetailDto
import com.filip.movieexplorer.data.network.dto.MovieSearchItemDto
import com.filip.movieexplorer.domain.model.FavoriteMovie
import com.filip.movieexplorer.domain.model.MovieDetail
import com.filip.movieexplorer.domain.model.MovieSummary

/**
 * Mapping utilities between DTOs and domain models.
 */
fun MovieSearchItemDto.toDomain(): MovieSummary? {
    // Vrátit null pokud chybí povinná pole
    val id = imdbId ?: return null
    val movieTitle = title ?: return null
    
    return MovieSummary(
        imdbId = id,
        title = movieTitle,
        year = year ?: "N/A",
        type = type ?: "movie",
        posterUrl = poster.takeUnless { it.isNullOrBlank() || it.equals("N/A", ignoreCase = true) } ?: ""
    )
}

fun MovieDetailDto.toDomain(): MovieDetail = MovieDetail(
    imdbId = imdbId.orEmpty(),
    title = title.orEmpty(),
    year = year.orEmpty(),
    director = director.orEmpty(),
    genre = genre.orEmpty(),
    plot = plot.orEmpty(),
    posterUrl = poster.takeUnless { it.isNullOrBlank() || it.equals("N/A", ignoreCase = true) }
        ?: "",
    actors = actors.orEmpty(),
    imdbRating = imdbRating.orEmpty()
)

fun FavoriteMovieEntity.toDomain(): FavoriteMovie = FavoriteMovie(
    imdbId = imdbId,
    title = title,
    year = year,
    director = director,
    genre = genre,
    posterUrl = posterUrl
)

fun FavoriteMovie.toEntity(): FavoriteMovieEntity = FavoriteMovieEntity(
    imdbId = imdbId,
    title = title,
    year = year,
    director = director,
    genre = genre,
    posterUrl = posterUrl
)

fun MovieSummary.toFavoriteMovie(): FavoriteMovie = FavoriteMovie(
    imdbId = imdbId,
    title = title,
    year = year,
    director = "",
    genre = "",
    posterUrl = posterUrl
)

fun MovieDetail.toFavoriteMovie(): FavoriteMovie = FavoriteMovie(
    imdbId = imdbId,
    title = title,
    year = year,
    director = director,
    genre = genre,
    posterUrl = posterUrl
)


