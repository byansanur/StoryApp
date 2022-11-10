package com.byandev.storyapp.data.model.dto

data class Result<out T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val code: String?,
) {
    private var loadingHasBeenHandled = false
    private var successHasBeenHandled = false
    private var errorHasBeenHandled = false

    fun getLoadingStateIfNotHandled() = if (loadingHasBeenHandled) null else loadingHasBeenHandled = true


    fun getSuccessStateIfNotHandled(): T? = if (successHasBeenHandled) null
    else {
        successHasBeenHandled = true
        data
    }

    fun getErrorStateIfNotHandled() = if (errorHasBeenHandled) null else errorHasBeenHandled = true

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T): Result<T> = Result(Status.SUCCESS, data, null, null)

        fun <T> error(message: String, data: T? = null, code: String? = null): Result<T> =
            Result(Status.ERROR, data, message, code)

        fun <T> loading(data: T? = null): Result<T> = Result(Status.LOADING, data, null, null)
    }
}