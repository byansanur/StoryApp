package com.byandev.storyapp.services

import com.byandev.storyapp.data.model.*
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ServicesImpl {

    fun postRegister(request: Register) : Single<ResponseBase>
    fun postLogin(request: Login) : Single<ResponseLogin>
    fun postStories(
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?
    ) : Single<ResponseBase>

}