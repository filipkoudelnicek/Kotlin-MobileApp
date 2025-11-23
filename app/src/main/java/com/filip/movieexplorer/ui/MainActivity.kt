package com.filip.movieexplorer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.filip.movieexplorer.MovieExplorerApp
import com.filip.movieexplorer.ui.navigation.MovieNavigation
import com.filip.movieexplorer.ui.theme.MovieExplorerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        
        val app = application as MovieExplorerApp
        val themePreferences = app.appContainer.themePreferences
        
        setContent {
            val isDarkMode by themePreferences.isDarkMode.collectAsState()
            
            MovieExplorerTheme(darkTheme = isDarkMode) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MovieNavigation()
                }
            }
        }
    }
}

