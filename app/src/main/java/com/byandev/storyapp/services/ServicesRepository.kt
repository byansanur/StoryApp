package com.byandev.storyapp.services

import com.byandev.storyapp.data.model.Login
import com.byandev.storyapp.data.model.Register
import com.byandev.storyapp.data.model.ResponseBase
import com.byandev.storyapp.data.model.ResponseLogin
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

class ServicesRepository @Inject constructor(
    private val apiServices: ApiServices
) : ServicesImpl {
    override fun postRegister(request: Register): Single<ResponseBase> =
        apiServices.postRegisterNewUser(request)

    override fun postLogin(request: Login): Single<ResponseLogin> =
        apiServices.postLoginUser(request)
}