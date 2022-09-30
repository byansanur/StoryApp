package com.byandev.storyapp.utils

import com.byandev.storyapp.data.model.Register
import com.byandev.storyapp.data.model.ResponseBase

object FakerResponseBase {
    val responseBaseSuccessfulRegisterFaker = ResponseBase(
        error = false,
        message = "User Created"
    )
}