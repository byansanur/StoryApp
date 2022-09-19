package com.byandev.storyapp.services

import com.byandev.storyapp.data.model.*
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

class ServicesRepository @Inject constructor(
    private val apiServices: ApiServices
) : ServicesImpl {
    override fun postRegister(request: Register): Single<ResponseBase> =
        apiServices.postRegisterNewUser(request)

    override fun postLogin(request: Login): Single<ResponseLogin> =
        apiServices.postLoginUser(request)

    override fun postStories(
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?
    ): Single<ResponseBase> =
        apiServices.postStories(
            description = description,
            image = photo,
            lat = lat,
            lon = lon
        )
}