package com.byandev.storyapp.domain.usecase

import com.byandev.storyapp.data.model.Login
import com.byandev.storyapp.data.model.Register
import com.byandev.storyapp.repository.repo.AuthRepository

class UserLoginUseCase(private val repo: AuthRepository) {
    operator fun invoke(request: Login) = repo.loginUser(login = request)
}