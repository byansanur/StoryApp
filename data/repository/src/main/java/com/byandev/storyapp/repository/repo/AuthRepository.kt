package com.byandev.storyapp.repository.repo

import com.byandev.storyapp.data.model.Login
import com.byandev.storyapp.data.model.Register
import com.byandev.storyapp.remote.datasource.RemoteDatasource
import com.byandev.storyapp.repository.utils.AppDispatcher
import com.byandev.storyapp.repository.utils.resultFlow

class AuthRepository(
    private val remoteDatasource: RemoteDatasource,
    private val dispatcher: AppDispatcher
) {
    fun registeringUser(register: Register) = resultFlow(
        networkCall = { remoteDatasource.postRegistration(register) },
        dispatcher = dispatcher
    )

    fun loginUser(login: Login) = resultFlow(
        networkCall = { remoteDatasource.postLoginUser(login) },
        dispatcher = dispatcher
    )
}