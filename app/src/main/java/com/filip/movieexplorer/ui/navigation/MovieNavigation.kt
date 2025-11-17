package com.filip.movieexplorer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.filip.movieexplorer.ui.screens.FavoritesScreen
import com.filip.movieexplorer.ui.screens.HomeRoute
import com.filip.movieexplorer.ui.screens.MovieDetailScreen

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
            MovieDetailScreen(
                imdbId = imdbId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(NavDestinations.FAVORITES) {
            FavoritesScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
    
    return navController
}

