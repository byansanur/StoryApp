package com.byandev.storyapp.services

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.byandev.storyapp.data.model.*
import com.byandev.storyapp.data.paging_source.StoryPagingSource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

class ServicesRepository @Inject constructor(
    private val apiServices: ApiServices
) {
    fun postRegister(request: Register): Single<ResponseBase> =
        apiServices.postRegisterNewUser(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    fun postLogin(request: Login): Single<ResponseLogin> =
        apiServices.postLoginUser(request).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    fun getListStory(location: Int) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true,
                initialLoadSize = 5
            ),
            pagingSourceFactory = { StoryPagingSource(apiServices = apiServices, location = location) }
        ).flow

    fun postStories(
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
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    fun getStoryLocation() : Single<ResponseAllStories> =
        apiServices.getStoriesLocation().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}