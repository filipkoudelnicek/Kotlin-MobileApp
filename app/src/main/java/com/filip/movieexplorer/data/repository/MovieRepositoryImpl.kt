package com.filip.movieexplorer.data.repository

import android.util.Log
import com.filip.movieexplorer.data.mapper.toDomain
import com.filip.movieexplorer.data.network.OmdbApiConfig
import com.filip.movieexplorer.data.network.OmdbApiService
import com.filip.movieexplorer.domain.model.MovieSummary
import com.filip.movieexplorer.domain.repository.MovieRepository

class MovieRepositoryImpl(
    private val apiService: OmdbApiService
) : MovieRepository {

    override suspend fun searchMovies(query: String): Result<List<MovieSummary>> = runCatching {
        val response = apiService.searchMovies(
            apikey = OmdbApiConfig.API_KEY,
            query = query
        )

        if (response.response.equals("False", ignoreCase = true)) {
            throw IllegalStateException(response.error ?: "Unknown error")
        }

        val movies = response.search?.map { it.toDomain() }.orEmpty()
        Log.d(TAG, "Fetched ${movies.size} movies for query: $query")
        movies
    }

    companion object {
        private const val TAG = "MovieRepository"
    }
}


