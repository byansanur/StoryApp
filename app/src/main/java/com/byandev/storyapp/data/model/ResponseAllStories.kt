package com.byandev.storyapp.data.model
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class ResponseAllStories(
    @Json(name = "error")
    val error: Boolean,
    @Json(name = "listStory")
    val listStory: List<Story>,
    @Json(name = "message")
    val message: String
)

@JsonClass(generateAdapter = true)
data class Story(
    @Json(name = "createdAt")
    val createdAt: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "id")
    val id: String,
    @Json(name = "lat")
    val lat: Double,
    @Json(name = "lon")
    val lon: Double,
    @Json(name = "name")
    val name: String,
    @Json(name = "photoUrl")
    val photoUrl: String
)