package com.filip.movieexplorer.data.repository

import android.util.Log
import com.filip.movieexplorer.data.local.dao.FavoriteMovieDao
import com.filip.movieexplorer.data.mapper.toDomain
import com.filip.movieexplorer.data.mapper.toEntity
import com.filip.movieexplorer.data.network.OmdbApiConfig
import com.filip.movieexplorer.data.network.OmdbApiService
import com.filip.movieexplorer.domain.model.FavoriteMovie
import com.filip.movieexplorer.domain.model.MovieDetail
import com.filip.movieexplorer.domain.model.MovieSummary
import com.filip.movieexplorer.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

class MovieRepositoryImpl(
    private val apiService: OmdbApiService,
    private val favoriteDao: FavoriteMovieDao
) : MovieRepository {

    // In-memory cache for search results (query -> movies)
    private val searchCache = ConcurrentHashMap<String, List<MovieSummary>>()
    
    // In-memory cache for movie details (imdbId -> movieDetail)
    private val detailCache = ConcurrentHashMap<String, MovieDetail>()

    override suspend fun searchMovies(query: String): Result<List<MovieSummary>> = withContext(Dispatchers.IO) {
        runCatching {
            val normalizedQuery = query.trim().lowercase()
            
            // Check cache first
            searchCache[normalizedQuery]?.let { cachedMovies ->
                Log.d(TAG, "Returning cached results for query: $query (${cachedMovies.size} movies)")
                return@runCatching cachedMovies
            }
            
            Log.d(TAG, "Searching movies with query: $query")
            val response = apiService.searchMovies(
                apikey = OmdbApiConfig.API_KEY,
                query = query
            )

            Log.d(TAG, "API Response: ${response.response}")

            if (response.response.equals("False", ignoreCase = true)) {
                val errorMsg = response.error ?: "Unknown error"
                Log.e(TAG, "API Error: $errorMsg")
                throw IllegalStateException(errorMsg)
            }

            // Remove duplicates by imdbId and filter null values
            val movies = response.search
                ?.mapNotNull { it.toDomain() }
                ?.distinctBy { it.imdbId }
                .orEmpty()
            
            // Cache the results
            searchCache[normalizedQuery] = movies
            
            Log.d(TAG, "Fetched ${movies.size} unique movies for query: $query")
            movies
        }
    }

    override suspend fun getMovieDetail(imdbId: String): Result<MovieDetail> = withContext(Dispatchers.IO) {
        runCatching {
            // Check cache first
            detailCache[imdbId]?.let { cachedDetail ->
                Log.d(TAG, "Returning cached details for imdbId: $imdbId")
                return@runCatching cachedDetail
            }
            
            val response = apiService.getMovieDetails(
                apikey = OmdbApiConfig.API_KEY,
                imdbId = imdbId,
                plot = "full"
            )

            if (response.response.equals("False", ignoreCase = true)) {
                throw IllegalStateException(response.error ?: "Unknown error")
            }

            val movieDetail = response.toDomain()
            
            // Cache the result
            detailCache[imdbId] = movieDetail
            
            Log.d(TAG, "Fetched details for imdbId: $imdbId")
            movieDetail
        }
    }

    override fun observeFavorites(): Flow<List<FavoriteMovie>> {
        return favoriteDao.getFavorites().map { favorites ->
            favorites.map { it.toDomain() }
        }
    }

    override fun isFavorite(imdbId: String): Flow<Boolean> = favoriteDao.isFavorite(imdbId)

    override suspend fun addFavorite(movie: FavoriteMovie) {
        favoriteDao.insertFavorite(movie.toEntity())
    }

    override suspend fun removeFavorite(imdbId: String) {
        favoriteDao.deleteById(imdbId)
    }

    companion object {
        private const val TAG = "MovieRepository"
    }
}


