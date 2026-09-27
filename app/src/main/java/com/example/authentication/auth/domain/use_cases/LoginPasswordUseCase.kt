package com.example.authentication.auth.domain.use_cases

import com.example.authentication.auth.domain.model.AuthTokens
import com.example.authentication.auth.domain.repository.AuthRepository
import com.example.authentication.core.domain.DataError
import com.example.authentication.core.domain.Result

class LoginPasswordUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(phone: String ,password: String): Result<AuthTokens, DataError> {
        return repository.logInWithPassword(phone,password)
    }
}