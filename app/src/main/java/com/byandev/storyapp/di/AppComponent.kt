package com.byandev.storyapp.di

//import com.byandev.storyapp.common.di.commonModule
import com.byandev.storyapp.domain.di.domainModule
import com.byandev.storyapp.presentation.di.presentationModule
import com.byandev.storyapp.presentation.di.viewModelModule
import com.byandev.storyapp.remote.di.remoteModule
import com.byandev.storyapp.repository.di.repositoryModule

val appComponent = listOf(
    remoteModule,
//    commonModule,
    repositoryModule,
    domainModule,
    presentationModule,
    viewModelModule
)