package com.byandev.storyapp.services

import com.byandev.storyapp.data.model.*
import io.reactivex.rxjava3.core.Single

interface ServicesImpl {

    fun postRegister(request: Register) : Single<ResponseBase>
    fun postLogin(request: Login) : Single<ResponseLogin>

}