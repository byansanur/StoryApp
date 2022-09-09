package com.byandev.storyapp.utils

import com.byandev.storyapp.data.model.ErrorBody
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

const val BASE_URL = "https://story-api.dicoding.dev/v1/"

fun handlingError(error: Throwable) : String {
    return when (error) {
        is HttpException -> getMessageError(error.response()?.errorBody()?.string())
        is TimeoutException, is SocketTimeoutException -> "Connection timed out..."
        is ConnectException, is SocketException -> "Connect error occurred"
        is UnknownHostException -> "No address associated with hostname"
        else -> "Unknown error occurred"
    }
}

@Throws(IOException::class)
fun getMessageError(errorBody: String?): String {
    return if (errorBody.isNullOrEmpty()) {
        val gson = Gson()
        val type = object : TypeToken<ErrorBody>() {}.type
        val errorResponse: ErrorBody = gson.fromJson(errorBody, type)
        errorResponse.message
    } else {
        try {
            val gson = Gson()
            val type = object : TypeToken<ErrorBody>() {}.type
            val errorResponse: ErrorBody = gson.fromJson(errorBody, type)
            errorResponse.message
        } catch (e: Exception) {
            e.printStackTrace()
            "Failed to read server"
        }
    }
}