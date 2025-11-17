package com.filip.movieexplorer.ui.detail

import com.filip.movieexplorer.domain.model.MovieDetail

data class MovieDetailUiState(
    val isLoading: Boolean = false,
    val movie: MovieDetail? = null,
    val errorMessage: String? = null
)


