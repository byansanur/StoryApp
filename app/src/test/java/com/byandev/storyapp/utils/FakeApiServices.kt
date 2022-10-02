package com.byandev.storyapp.utils

import com.byandev.storyapp.data.model.*
import com.byandev.storyapp.services.ApiServices
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiServices : ApiServices {

    private val listStoryFake = FakerListStory
    private val responseBaseFake = FakerResponseBase
    private val responseLoginFake = FakerResponseLogin

    override fun postRegisterNewUser(request: Register): Single<ResponseBase> {
        return Single.just(responseBaseFake.responseBaseSuccessfulRegisterFaker)
    }

    override fun postLoginUser(request: Login): Single<ResponseLogin> {
        return Single.just(responseLoginFake.generateLoginFake())
    }

    override fun postStories(
        description: RequestBody,
        image: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?
    ): Single<ResponseBase> {
        return Single.just(responseBaseFake.responseBaseSuccessfulRegisterFaker)
    }

    override suspend fun getAllStories(page: Int, size: Int, location: Int?): ResponseAllStories {
        return listStoryFake.generateListStory()
    }

    override fun getStoriesLocation(location: Int): Single<ResponseAllStories> {
        return Single.just(listStoryFake.generateListStory())
    }
}