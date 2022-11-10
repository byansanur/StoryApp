package com.byandev.storyapp.remote.datasource

import com.byandev.storyapp.data.model.Register
import com.byandev.storyapp.remote.getResult
import com.byandev.storyapp.remote.services.ApiServices

class RemoteDatasource(private val apiServices: ApiServices) {

    suspend fun postRegistration(register: Register) =
        getResult { apiServices.postRegisterNewUser(register) }
}