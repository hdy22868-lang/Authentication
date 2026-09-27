package com.example.authentication.auth.domain.repository

import com.example.authentication.auth.domain.model.AuthTokens
import com.example.authentication.core.domain.DataError
import com.example.authentication.core.domain.Result

interface AuthRepository {
    suspend fun signUp(phone : String): Result<Unit, DataError>
    suspend fun verify(phone : String , otp : String): Result<AuthTokens, DataError>

    suspend fun logInWithPassword(phone : String , password : String): Result<AuthTokens, DataError>
    suspend fun logInWithOtp(phone : String , otp : String): Result<AuthTokens, DataError>

    suspend fun resentOtp(phone : String): Result<Unit, DataError>
    suspend fun logout(): Result<Unit, DataError>
}