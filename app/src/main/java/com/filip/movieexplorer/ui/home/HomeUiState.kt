package com.filip.movieexplorer.ui.home

import com.filip.movieexplorer.domain.model.MovieSummary

data class HomeUiState(
    val isLoading: Boolean = false,
    val movies: List<MovieSummary> = emptyList(),
    val errorMessage: String? = null,
    val lastQuery: String = "",
    val favoriteIds: Set<String> = emptySet()
)


