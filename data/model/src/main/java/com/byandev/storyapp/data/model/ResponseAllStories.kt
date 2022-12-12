package com.byandev.storyapp.data.model

import com.byandev.storyapp.data.model.dto.Stories
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ResponseAllStories(
    @Json(name = "error")
    val error: Boolean,
    @Json(name = "listStory")
    val listStory: List<Stories>,
    @Json(name = "message")
    val message: String
)