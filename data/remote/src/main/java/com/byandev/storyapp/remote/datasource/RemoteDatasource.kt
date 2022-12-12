package com.byandev.storyapp.remote.datasource

import com.byandev.storyapp.data.model.Login
import com.byandev.storyapp.data.model.Register
import com.byandev.storyapp.remote.getResult
import com.byandev.storyapp.remote.services.ApiServices
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RemoteDatasource(private val apiServices: ApiServices) {

    suspend fun postRegistration(register: Register) =
        getResult { apiServices.postRegisterNewUser(register) }

    suspend fun postLoginUser(login: Login) =
        getResult { apiServices.postLoginUser(login) }

    suspend fun getAllStory(page: Int, size: Int, location: Int) = getResult {
        apiServices.getAllStories(page, size, location)
    }

    suspend fun postStory(
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?
    ) = getResult { apiServices.postStories(description, photo, lat, lon) }
}