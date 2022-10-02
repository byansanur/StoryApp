package com.byandev.storyapp.utils

import com.byandev.storyapp.data.model.LoginResult
import com.byandev.storyapp.data.model.ResponseLogin

object FakerResponseLogin {

    fun generateLoginFake() : ResponseLogin {
        return ResponseLogin(
            error = false,
            loginResult = LoginResult(
                name = "aaa",
                token = "eyasddsd",
                userId = "user-1"
            ),
            message = "Success"
        )
    }
}