package com.filip.movieexplorer.domain.repository

import com.filip.movieexplorer.domain.model.FavoriteMovie
import com.filip.movieexplorer.domain.model.MovieDetail
import com.filip.movieexplorer.domain.model.MovieSummary
import kotlinx.coroutines.flow.Flow

/**
 * Repository abstraction for movie data.
 * Later commits will expand this interface to include favorites (Room).
 */
interface MovieRepository {
    suspend fun searchMovies(query: String): Result<List<MovieSummary>>
    suspend fun getMovieDetail(imdbId: String): Result<MovieDetail>
    fun observeFavorites(): Flow<List<FavoriteMovie>>
    fun isFavorite(imdbId: String): Flow<Boolean>
    suspend fun addFavorite(movie: FavoriteMovie)
    suspend fun removeFavorite(imdbId: String)
}


