package com.byandev.storyapp.repository.utils

import kotlinx.coroutines.CoroutineDispatcher

class AppDispatcher(
    val main: CoroutineDispatcher,
    val io: CoroutineDispatcher
)