package com.byandev.storyapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Register(
    @Json(name = "name")
    var name: String,
    @Json(name = "email")
    var email: String,
    @Json(name = "password")
    var password: String
)

@JsonClass(generateAdapter = true)
data class Login(
    @Json(name = "email")
    var email: String,
    @Json(name = "password")
    var password: String
)