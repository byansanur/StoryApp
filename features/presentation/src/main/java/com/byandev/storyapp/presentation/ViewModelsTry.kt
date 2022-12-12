package com.byandev.storyapp.presentation

import com.byandev.storyapp.common.base.BaseViewModel
import com.byandev.storyapp.data.model.Register
import com.byandev.storyapp.domain.usecase.RegisteringNewUserUseCase
import kotlinx.coroutines.flow.map

class ViewModelsTry(private val useCase: RegisteringNewUserUseCase) : BaseViewModel() {

    fun register(register: Register) =
        useCase.invoke(register).map { data -> data.data }
}