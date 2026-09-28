package com.example.authentication.auth.domain.use_cases

import com.example.authentication.auth.domain.model.AuthTokens
import com.example.authentication.auth.domain.repository.AuthRepository
import com.example.authentication.core.domain.DataError
import com.example.authentication.core.domain.Result

class LogInOtpUseCase (
    private val repository: AuthRepository
) {
    suspend operator fun invoke(phone: String): Result<Unit, DataError> {
        return repository.logInWithOtp(phone)
    }
}
