package com.filip.movieexplorer.data.di

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Centralized theme preferences manager.
 * This allows SettingsViewModel and MainActivity to share the same state.
 */
class ThemePreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "app_settings",
        Context.MODE_PRIVATE
    )

    private val _isDarkMode = MutableStateFlow(
        sharedPreferences.getBoolean(KEY_DARK_MODE, false)
    )
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    fun setDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
        sharedPreferences.edit()
            .putBoolean(KEY_DARK_MODE, enabled)
            .apply()
    }

    companion object {
        private const val KEY_DARK_MODE = "dark_mode"
    }
}

