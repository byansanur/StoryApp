package com.byandev.storyapp.repository.di

import com.byandev.storyapp.repository.repo.AuthRepository
import com.byandev.storyapp.repository.repo.StoryRepository
import com.byandev.storyapp.repository.utils.AppDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val repositoryModule = module {
    factory { AppDispatcher(Dispatchers.Main, Dispatchers.IO) }

    factory { AuthRepository(get(), get()) }
    factory { StoryRepository(get(), get()) }
}