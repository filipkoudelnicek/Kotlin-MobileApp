package com.filip.movieexplorer.domain.repository

import com.filip.movieexplorer.domain.model.MovieDetail
import com.filip.movieexplorer.domain.model.MovieSummary

/**
 * Repository abstraction for movie data.
 * Later commits will expand this interface to include favorites (Room).
 */
interface MovieRepository {
    suspend fun searchMovies(query: String): Result<List<MovieSummary>>
    suspend fun getMovieDetail(imdbId: String): Result<MovieDetail>
}


