package com.byandev.storyapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseBase(
    @Json(name = "error")
    val error: Boolean,
    @Json(name = "message")
    val message: String
)