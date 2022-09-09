package com.byandev.storyapp.services

import com.byandev.storyapp.data.model.ResponseAllStories
import com.byandev.storyapp.data.model.ResponseBase
import com.byandev.storyapp.data.model.ResponseLogin
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiServices {

    @POST("register")
    fun postRegisterNewUser(
        @Body name: String,
        @Body email: String,
        @Body password: String
    ): Single<ResponseBase>


    @POST("login")
    fun postLoginUser(
        @Body email: String,
        @Body password: String
    ): Single<ResponseLogin>

    @Multipart
    @POST("stories")
    fun postStories(
        @Part("description") description: String,
        @Part image: MultipartBody.Part,
        @Part("lat") lat: Float?,
        @Part("lon") lon: Float?
    ) : Single<ResponseBase>

    @GET("stories")
    fun getAllStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int?
    ) : Single<ResponseAllStories>


}