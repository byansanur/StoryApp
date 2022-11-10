package com.byandev.storyapp.domain.usecase

import com.byandev.storyapp.data.model.Register
import com.byandev.storyapp.repository.repo.AuthRepository

class RegisteringNewUserUseCase(private val repo: AuthRepository) {
    operator fun invoke(request: Register) = repo.registeringUser(register = request)
}