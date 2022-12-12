package com.byandev.storyapp.common.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.byandev.storyapp.common.LocationUtils
import com.byandev.storyapp.common.SharedPrefManager
import com.byandev.storyapp.common.UtilsConnect
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

//val commonModule = module(override = true) {
//    single {
//        SharedPrefManager(androidContext())
//    }
//    single {
//        UtilsConnect(androidContext())
//    }
//    single {
//        LocationUtils(androidContext())
//    }
//}