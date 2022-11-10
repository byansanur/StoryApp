package com.byandev.storyapp.repository.utils

import com.byandev.storyapp.data.model.dto.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


fun <T> resultFlow(
    networkCall: suspend () -> Result<T>,
    dispatcher: AppDispatcher
) : Flow<Result<T>> = flow {
    emit(Result.loading(null))
    val response = networkCall()
    when (response.status) {
        Result.Status.SUCCESS -> {
            emit(Result.success(response.data!!))
        }
        Result.Status.ERROR -> {
            emit(
                Result.error(
                    data = null,
                    message = response.message!!,
                    code = response.code
                )
            )
        }
        Result.Status.LOADING -> {

        }
    }
}.flowOn(dispatcher.io)