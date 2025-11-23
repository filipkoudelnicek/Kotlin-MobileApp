package com.filip.movieexplorer.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.StateFlow
import com.filip.movieexplorer.MovieExplorerApp
import com.filip.movieexplorer.data.di.ThemePreferences

class SettingsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val themePreferences: ThemePreferences =
        (application as MovieExplorerApp).appContainer.themePreferences

    val isDarkMode: StateFlow<Boolean> = themePreferences.isDarkMode

    fun setDarkMode(enabled: Boolean) {
        themePreferences.setDarkMode(enabled)
    }

    companion object {
        fun provideFactory(
            application: Application
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                    return SettingsViewModel(application) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}

