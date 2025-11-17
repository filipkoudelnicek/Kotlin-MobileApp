package com.filip.movieexplorer.ui.navigation

/**
 * Central place to define navigation destinations.
 */
object NavDestinations {
    const val HOME = "home"
    const val DETAILS = "details/{imdbId}"
    const val FAVORITES = "favorites"
    const val LOCATION = "location"
    
    fun detailsRoute(imdbId: String) = "details/$imdbId"
}

