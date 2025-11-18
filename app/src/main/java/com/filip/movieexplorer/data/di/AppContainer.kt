package com.filip.movieexplorer.data.di

import android.content.Context
import androidx.room.Room
import com.filip.movieexplorer.data.local.MovieDatabase
import com.filip.movieexplorer.data.network.OmdbApiConfig
import com.filip.movieexplorer.data.repository.MovieRepositoryImpl
import com.filip.movieexplorer.domain.repository.MovieRepository

/**
 * Simple service locator for providing app-wide dependencies.
 * In future commits this could be replaced with a DI framework.
 */
class AppContainer(context: Context) {
    private val omdbApiService by lazy { OmdbApiConfig.createApiService() }
    private val database by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            "movie_explorer.db"
        ).fallbackToDestructiveMigration().build()
    }
    private val favoriteDao by lazy { database.favoriteMovieDao() }

    val movieRepository: MovieRepository by lazy {
        MovieRepositoryImpl(
            apiService = omdbApiService,
            favoriteDao = favoriteDao
        )
    }

    val themePreferences: ThemePreferences by lazy {
        ThemePreferences(context.applicationContext)
    }
}

