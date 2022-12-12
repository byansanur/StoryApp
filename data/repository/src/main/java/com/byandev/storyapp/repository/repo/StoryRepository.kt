package com.byandev.storyapp.repository.repo

import com.byandev.storyapp.remote.datasource.RemoteDatasource
import com.byandev.storyapp.repository.utils.AppDispatcher
import com.byandev.storyapp.repository.utils.resultFlow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(
    private val remoteDatasource: RemoteDatasource,
    private val appDispatcher: AppDispatcher
) {
    fun getAllStory(page: Int, size: Int, location: Int) = resultFlow(
        networkCall = { remoteDatasource.getAllStory(page, size, location) },
        dispatcher = appDispatcher
    )

    fun postStory(
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?
    ) = resultFlow(
        networkCall = { remoteDatasource.postStory(description, photo, lat, lon) },
        dispatcher = appDispatcher
    )
}