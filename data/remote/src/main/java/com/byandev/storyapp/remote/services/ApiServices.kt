package com.byandev.storyapp.remote.services

import com.byandev.storyapp.data.model.*
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiServices {

    @POST("register")
    suspend fun postRegisterNewUser(
        @Body request: Register,
    ): Response<ResponseBase>

    @POST("login")
    suspend fun postLoginUser(
        @Body request: Login
    ): Response<ResponseLogin>

    @Multipart
    @POST("stories")
    fun postStories(
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null
    ) : Response<ResponseBase>

    @GET("stories")
    suspend fun getAllStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int?
    ) : Response<ResponseAllStories>

    @GET("stories")
    fun getStoriesLocation(
        @Query("location") location: Int = 1
    ) : Single<ResponseAllStories>


}