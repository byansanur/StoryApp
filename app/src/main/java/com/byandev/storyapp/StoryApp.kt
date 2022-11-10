package com.byandev.storyapp

import android.app.Application
import com.byandev.storyapp.di.appComponent
import org.koin.core.context.startKoin
import timber.log.Timber

open class StoryApp : Application() {

    override fun onCreate() {
        super.onCreate()
        configureDi()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

    private fun configureDi() =
        startKoin {
            provideComponent()
        }

    open fun provideComponent() = appComponent
}