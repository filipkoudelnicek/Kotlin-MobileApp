package com.filip.movieexplorer.data.network

import com.filip.movieexplorer.data.network.dto.MovieDetailDto
import com.filip.movieexplorer.data.network.dto.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for OMDb API.
 */
interface OmdbApiService {
    /**
     * Search movies by title.
     * @param apikey API key for OMDb
     * @param query Search query (movie title)
     * @param type Type filter (should be "movie")
     * @param page Page number (optional, defaults to 1)
     */
    @GET("/")
    suspend fun searchMovies(
        @Query("apikey") apikey: String,
        @Query("s") query: String,
        @Query("type") type: String = "movie",
        @Query("page") page: Int = 1
    ): SearchResponseDto

    /**
     * Get movie details by IMDb ID.
     * @param apikey API key for OMDb
     * @param imdbId IMDb ID of the movie
     * @param plot Plot length ("short" or "full")
     */
    @GET("/")
    suspend fun getMovieDetails(
        @Query("apikey") apikey: String,
        @Query("i") imdbId: String,
        @Query("plot") plot: String = "full"
    ): MovieDetailDto
}

