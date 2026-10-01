package com.example.authentication.auth.data.repository

import com.example.authentication.auth.domain.model.AuthTokens
import com.example.authentication.auth.domain.model.User
import com.example.authentication.auth.domain.repository.AuthRepository
import com.example.authentication.core.domain.DataError
import com.example.authentication.core.domain.Result

class FakeAuthRepositoryImpl : AuthRepository {

    private val usersMemory = mutableMapOf<String, String>(
        "07829155438" to "ha7hu5_"
    )

    override suspend fun signUp(
        phone: String,
        password: String,
        name: String
    ): Result<Unit, DataError> {
        usersMemory[phone] = password
        return Result.Success(Unit)
    }

    override suspend fun verify(
        phone: String,
        otp: String
    ): Result<Pair<User, AuthTokens>, DataError> {
        val mockUser = User(id = 1, phoneNumber = phone, isVerified = true)
        val mockTokens = AuthTokens(accessToken = "fake_access_token", refreshToken = "fake_refresh_token")
        return Result.Success(Pair(mockUser, mockTokens))
    }

    override suspend fun logInWithPassword(
        phone: String,
        password: String
    ): Result<Pair<User, AuthTokens>, DataError> {
        val savedPassword = usersMemory[phone]
        if (savedPassword == null || savedPassword != password) {
            return Result.Error(DataError.Network.UNAUTHORIZED)
        }
        val mockUser = User(id = 1, phoneNumber = phone, isVerified = true)
        val mockTokens = AuthTokens(accessToken = "fake_access_token", refreshToken = "fake_refresh_token")
        return Result.Success(Pair(mockUser, mockTokens))
    }

    override suspend fun logInWithOtp(phone: String): Result<Unit, DataError> {
        return if (usersMemory.containsKey(phone)) {
            Result.Success(Unit)
        } else {
            Result.Error(DataError.Network.UNAUTHORIZED)
        }
    }

    override suspend fun resentOtp(phone: String): Result<Unit, DataError> {
        return Result.Success(Unit)
    }

    override suspend fun resetPassword(phone: String, newPassword: String): Result<Unit, DataError> {
        if (usersMemory.containsKey(phone)) {
            usersMemory[phone] = newPassword
            return Result.Success(Unit)
        }
        return Result.Error(DataError.Network.UNAUTHORIZED)
    }

    override suspend fun logout(): Result<Unit, DataError> {
        return Result.Success(Unit)
    }
}