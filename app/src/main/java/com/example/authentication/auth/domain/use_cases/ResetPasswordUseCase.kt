package com.example.authentication.auth.domain.use_cases


import com.example.authentication.auth.domain.repository.AuthRepository
import com.example.authentication.core.domain.DataError
import com.example.authentication.core.domain.Result

class ResetPasswordUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(phone: String, newPassword: String): Result<Unit, DataError> {
        return repository.resetPassword(phone, newPassword)
    }
}