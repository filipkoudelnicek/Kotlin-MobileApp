package com.filip.movieexplorer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.filip.movieexplorer.ui.navigation.MovieNavigation
import com.filip.movieexplorer.ui.theme.MovieExplorerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieExplorerTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MovieNavigation()
                }
            }
        }
    }
}

