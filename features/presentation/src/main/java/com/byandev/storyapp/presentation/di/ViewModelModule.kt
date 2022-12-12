package com.byandev.storyapp.presentation.di

import com.byandev.storyapp.presentation.PresentationViewModel
import com.byandev.storyapp.presentation.SharedViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { PresentationViewModel(get(), get(), get(), get()) }
//    viewModel { SharedViewModel() }
}