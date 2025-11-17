package com.filip.movieexplorer.data.di

import com.filip.movieexplorer.data.network.OmdbApiConfig
import com.filip.movieexplorer.data.repository.MovieRepositoryImpl
import com.filip.movieexplorer.domain.repository.MovieRepository

/**
 * Simple service locator for providing app-wide dependencies.
 * In future commits this could be replaced with a DI framework.
 */
class AppContainer {
    private val omdbApiService by lazy { OmdbApiConfig.createApiService() }

    val movieRepository: MovieRepository by lazy {
        MovieRepositoryImpl(omdbApiService)
    }
}


