package com.filip.movieexplorer.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.filip.movieexplorer.data.mapper.toFavoriteMovie
import com.filip.movieexplorer.domain.model.MovieSummary
import com.filip.movieexplorer.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

class HomeViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                movieRepository.observeFavorites().collectLatest { favorites ->
                    _uiState.value = _uiState.value.copy(
                        favoriteIds = favorites.map { it.imdbId }.toSet()
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error observing favorites", e)
            }
        }
    }

    fun search(query: String) {
        Log.d(TAG, "Search called with query: $query")
        if (query.isBlank()) {
            _uiState.value = HomeUiState(
                isLoading = false,
                movies = emptyList(),
                errorMessage = "Please enter a movie title",
                lastQuery = "",
                favoriteIds = _uiState.value.favoriteIds
            )
            return
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null,
            lastQuery = query
        )

        viewModelScope.launch {
            try {
                val result = movieRepository.searchMovies(query)
                val currentFavorites = _uiState.value.favoriteIds
                _uiState.value = result.fold(
                    onSuccess = { movies ->
                        Log.d(TAG, "Search successful: ${movies.size} movies found")
                        HomeUiState(
                            isLoading = false,
                            movies = movies,
                            errorMessage = null,
                            lastQuery = query,
                            favoriteIds = currentFavorites
                        )
                    },
                    onFailure = { throwable ->
                        Log.e(TAG, "Search failed", throwable)
                        HomeUiState(
                            isLoading = false,
                            movies = emptyList(),
                            errorMessage = throwable.message ?: "Unknown error",
                            lastQuery = query,
                            favoriteIds = currentFavorites
                        )
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error during search", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Unexpected error: ${e.message}"
                )
            }
        }
    }

    fun toggleFavorite(movie: MovieSummary) {
        viewModelScope.launch {
            try {
                val isFavorite = _uiState.value.favoriteIds.contains(movie.imdbId)
                if (isFavorite) {
                    movieRepository.removeFavorite(movie.imdbId)
                } else {
                    movieRepository.addFavorite(movie.toFavoriteMovie())
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error toggling favorite", e)
            }
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
        
        fun provideFactory(
            repository: MovieRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                    return HomeViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}


