package com.byandev.storyapp.data.model

data class Register(
    var name: String,
    var email: String,
    var password: String
)

data class Login(
    var email: String,
    var password: String
)

data class AddStory(
    var description: String,
)