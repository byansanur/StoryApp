package com.byandev.storyapp.services

import com.byandev.storyapp.data.model.*
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiServices {

    @POST("register")
    fun postRegisterNewUser(
        @Body request: Register,
    ): Single<ResponseBase>


    @POST("login")
    fun postLoginUser(
        @Body request: Login
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
    suspend fun getAllStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int?
    ) : ResponseAllStories


}