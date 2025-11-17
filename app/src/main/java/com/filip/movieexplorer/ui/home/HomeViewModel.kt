package com.filip.movieexplorer.ui.home

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
            movieRepository.observeFavorites().collectLatest { favorites ->
                _uiState.value = _uiState.value.copy(
                    favoriteIds = favorites.map { it.imdbId }.toSet()
                )
            }
        }
    }

    fun search(query: String) {
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
            val result = movieRepository.searchMovies(query)
            _uiState.value = result.fold(
                onSuccess = { movies ->
                    _uiState.value.copy(
                        isLoading = false,
                        movies = movies,
                        errorMessage = null,
                        lastQuery = query
                    )
                },
                onFailure = { throwable ->
                    _uiState.value.copy(
                        isLoading = false,
                        movies = emptyList(),
                        errorMessage = throwable.message ?: "Unknown error",
                        lastQuery = query
                    )
                }
            )
        }
    }

    fun toggleFavorite(movie: MovieSummary) {
        viewModelScope.launch {
            val isFavorite = _uiState.value.favoriteIds.contains(movie.imdbId)
            if (isFavorite) {
                movieRepository.removeFavorite(movie.imdbId)
            } else {
                movieRepository.addFavorite(movie.toFavoriteMovie())
            }
        }
    }

    companion object {
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


