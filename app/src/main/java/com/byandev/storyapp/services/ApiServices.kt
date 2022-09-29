package com.byandev.storyapp.services

import com.byandev.storyapp.data.model.*
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null
    ) : Single<ResponseBase>

    @GET("stories")
    suspend fun getAllStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int?
    ) : ResponseAllStories

    @GET("stories")
    fun getStoriesLocation(
        @Query("location") location: Int = 1
    ) : Single<ResponseAllStories>


}