package com.filip.movieexplorer.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.filip.movieexplorer.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    fun loadMovie(imdbId: String) {
        if (_uiState.value.isLoading) return

        _uiState.value = MovieDetailUiState(isLoading = true)
        viewModelScope.launch {
            val detailResult = movieRepository.getMovieDetail(imdbId)
            _uiState.value = detailResult.fold(
                onSuccess = { detail ->
                    MovieDetailUiState(
                        isLoading = false,
                        movie = detail,
                        errorMessage = null
                    )
                },
                onFailure = { throwable ->
                    MovieDetailUiState(
                        isLoading = false,
                        movie = null,
                        errorMessage = throwable.message ?: "Unknown error"
                    )
                }
            )
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


