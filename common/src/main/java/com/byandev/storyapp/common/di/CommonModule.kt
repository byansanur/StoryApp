package com.byandev.storyapp.common.di

import com.byandev.storyapp.common.SharedPrefManager
import com.byandev.storyapp.common.UtilsConnect
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val commonModule = module {
    single {
        SharedPrefManager(androidApplication().applicationContext)
    }
    single {
        UtilsConnect(androidApplication().applicationContext)
    }
}