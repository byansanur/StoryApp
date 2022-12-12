package com.byandev.storyapp.domain.di

import com.byandev.storyapp.domain.usecase.RegisteringNewUserUseCase
import com.byandev.storyapp.domain.usecase.StoryPagingSource
import org.koin.dsl.module

val domainModule = module {
    factory { RegisteringNewUserUseCase(get()) }
    factory { StoryPagingSource(get(), get()) }
}