package com.filip.movieexplorer.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.filip.movieexplorer.data.mapper.toFavoriteMovie
import com.filip.movieexplorer.domain.repository.MovieRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()
    private var favoriteObserverJob: Job? = null

    fun loadMovie(imdbId: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )
        observeFavorite(imdbId)
        viewModelScope.launch {
            val detailResult = movieRepository.getMovieDetail(imdbId)
            _uiState.value = detailResult.fold(
                onSuccess = { detail ->
                    _uiState.value.copy(
                        isLoading = false,
                        movie = detail,
                        errorMessage = null
                    )
                },
                onFailure = { throwable ->
                    _uiState.value.copy(
                        isLoading = false,
                        movie = null,
                        errorMessage = throwable.message ?: "Unknown error"
                    )
                }
            )
        }
    }

    private fun observeFavorite(imdbId: String) {
        favoriteObserverJob?.cancel()
        favoriteObserverJob = viewModelScope.launch {
            movieRepository.isFavorite(imdbId).collectLatest { isFavorite ->
                _uiState.value = _uiState.value.copy(isFavorite = isFavorite)
            }
        }
    }

    fun toggleFavorite() {
        val movie = _uiState.value.movie ?: return
        viewModelScope.launch {
            if (_uiState.value.isFavorite) {
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
                if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
                    return MovieDetailViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}


