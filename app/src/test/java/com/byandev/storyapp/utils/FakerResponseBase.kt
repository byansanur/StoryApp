package com.byandev.storyapp.utils

import com.byandev.storyapp.data.model.Register
import com.byandev.storyapp.data.model.ResponseBase

object FakerResponseBase {
    val responseBaseSuccessfulRegisterFaker = ResponseBase(
        error = false,
        message = "User Created"
    )

    val responseBaseErrorRegisterFaker = ResponseBase(
        error = true,
        message = "\"email\" must be a valid email"
    )

    val responseBaseErrorNameRegisterFaker = ResponseBase(
        error = true,
        message = "\"name\" is not allowed to be empty"
    )
}