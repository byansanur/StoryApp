package com.byandev.storyapp.services

import com.byandev.storyapp.data.model.Login
import com.byandev.storyapp.data.model.Register
import com.byandev.storyapp.data.model.ResponseBase
import com.byandev.storyapp.data.model.ResponseLogin
import io.reactivex.rxjava3.core.Single

interface ServicesImpl {

    fun postRegister(request: Register) : Single<ResponseBase>
    fun postLogin(request: Login) : Single<ResponseLogin>

}