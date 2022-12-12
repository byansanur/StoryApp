package com.byandev.storyapp.data.model.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Stories(
    @Json(name = "createdAt")
    val createdAt: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "id")
    val id: String,
    @Json(name = "lat")
    val lat: Double?,
    @Json(name = "lon")
    val lon: Double?,
    @Json(name = "name")
    val name: String,
    @Json(name = "photoUrl")
    val photoUrl: String
) : Serializable