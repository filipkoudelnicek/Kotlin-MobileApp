package com.filip.movieexplorer.data.mapper

import com.filip.movieexplorer.data.network.dto.MovieSearchItemDto
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


