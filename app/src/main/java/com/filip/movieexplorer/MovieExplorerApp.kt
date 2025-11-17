package com.filip.movieexplorer

import android.app.Application
import com.filip.movieexplorer.data.di.AppContainer

class MovieExplorerApp : Application() {

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}

