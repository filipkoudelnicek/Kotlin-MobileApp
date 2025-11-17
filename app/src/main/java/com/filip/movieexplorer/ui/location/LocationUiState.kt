package com.filip.movieexplorer.ui.location

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long
)

data class LocationUiState(
    val hasPermission: Boolean = false,
    val isLoading: Boolean = false,
    val location: LocationData? = null,
    val errorMessage: String? = null
)


