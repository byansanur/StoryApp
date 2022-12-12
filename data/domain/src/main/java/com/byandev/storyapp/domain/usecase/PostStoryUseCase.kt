package com.byandev.storyapp.domain.usecase

import com.byandev.storyapp.repository.repo.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostStoryUseCase(private val storyRepository: StoryRepository) {
    operator fun invoke(
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody?,
        lon: RequestBody?
    ) = storyRepository.postStory(
        description, photo, lat, lon
    )
}