package com.byandev.storyapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class StoryApp : Application() {

    companion object {
        lateinit var instance: StoryApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}