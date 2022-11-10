package com.byandev.storyapp.domain.di

import com.byandev.storyapp.domain.usecase.RegisteringNewUserUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { RegisteringNewUserUseCase(get()) }
}