package com.example.authentication.auth.domain.use_cases

import com.example.authentication.auth.domain.repository.AuthRepository

class IsLoggedInUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Boolean = repository.isLoggedIn()
}