package com.filip.movieexplorer.ui.favorites

import com.filip.movieexplorer.domain.model.FavoriteMovie

data class FavoritesUiState(
    val isLoading: Boolean = true,
    val favorites: List<FavoriteMovie> = emptyList()
)


