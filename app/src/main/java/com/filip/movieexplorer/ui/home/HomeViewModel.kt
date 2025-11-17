package com.filip.movieexplorer.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.filip.movieexplorer.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun search(query: String) {
        if (query.isBlank()) {
            _uiState.value = HomeUiState(
                isLoading = false,
                movies = emptyList(),
                errorMessage = "Please enter a movie title",
                lastQuery = ""
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
                    HomeUiState(
                        isLoading = false,
                        movies = movies,
                        errorMessage = null,
                        lastQuery = query
                    )
                },
                onFailure = { throwable ->
                    HomeUiState(
                        isLoading = false,
                        movies = emptyList(),
                        errorMessage = throwable.message ?: "Unknown error",
                        lastQuery = query
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
                if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                    return HomeViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}


