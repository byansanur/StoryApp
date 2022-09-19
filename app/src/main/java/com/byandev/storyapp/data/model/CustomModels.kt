package com.byandev.storyapp.data.model

import java.io.Serializable

data class IntroItem(
    val title : String,
    val image : Int,
    val description : String
)

data class PostLocation(
    var latitude: String,
    var longitude: String
) : Serializable