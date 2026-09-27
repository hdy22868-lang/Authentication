package com.example.authentication.auth.domain.use_cases

import com.example.authentication.auth.domain.repository.AuthRepository
import com.example.authentication.core.domain.DataError
import com.example.authentication.core.domain.Result

class LogoutUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit, DataError>{
        return repository.logout()
    }
}