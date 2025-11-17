package com.filip.movieexplorer.data.mapper

import com.filip.movieexplorer.data.network.dto.MovieDetailDto
import com.filip.movieexplorer.data.network.dto.MovieSearchItemDto
import com.filip.movieexplorer.domain.model.MovieDetail
import com.filip.movieexplorer.domain.model.MovieSummary

/**
 * Mapping utilities between DTOs and domain models.
 */
fun MovieSearchItemDto.toDomain(): MovieSummary = MovieSummary(
    imdbId = imdbId,
    title = title,
    year = year,
    type = type,
    posterUrl = poster.takeUnless { it.equals("N/A", ignoreCase = true) } ?: ""
)

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


