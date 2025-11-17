package com.filip.movieexplorer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.filip.movieexplorer.ui.screens.FavoritesRoute
import com.filip.movieexplorer.ui.screens.HomeRoute
import com.filip.movieexplorer.ui.screens.LocationRoute
import com.filip.movieexplorer.ui.screens.MovieDetailRoute

@Composable
fun MovieNavigation(
    navController: NavHostController = rememberNavController()
): NavHostController {
    NavHost(
        navController = navController,
        startDestination = NavDestinations.HOME
    ) {
        composable(NavDestinations.HOME) {
            HomeRoute(
                onNavigateToDetails = { imdbId ->
                    navController.navigate(NavDestinations.detailsRoute(imdbId))
                },
                onNavigateToFavorites = {
                    navController.navigate(NavDestinations.FAVORITES)
                },
                onNavigateToLocation = {
                    navController.navigate(NavDestinations.LOCATION)
                }
            )
        }
        
        composable(
            route = NavDestinations.DETAILS,
            arguments = listOf(
                navArgument("imdbId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val imdbId = backStackEntry.arguments?.getString("imdbId") ?: ""
            MovieDetailRoute(
                imdbId = imdbId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(NavDestinations.FAVORITES) {
            FavoritesRoute(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onMovieClick = { imdbId ->
                    navController.navigate(NavDestinations.detailsRoute(imdbId))
                }
            )
        }

        composable(NavDestinations.LOCATION) {
            LocationRoute(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
    
    return navController
}

