package com.example.authentication.auth.data.repository

import com.example.authentication.auth.domain.model.AuthTokens
import com.example.authentication.auth.domain.model.User
import com.example.authentication.auth.domain.repository.AuthRepository
import com.example.authentication.core.domain.DataError
import com.example.authentication.core.domain.Result

class FakeAuthRepositoryImpl : AuthRepository {

    override suspend fun signUp(
        phone: String,
        password: String,
        name: String
    ): Result<Unit, DataError> {
        return Result.Success(Unit)
    }

    override suspend fun verify(
        phone: String,
        otp: String
    ): Result<Pair<User, AuthTokens>, DataError> {
        val fakeUser = User(id = 1, phoneNumber = phone, isVerified = true)
        val fakeTokens = AuthTokens(accessToken = "fake_access_token", refreshToken = "fake_refresh_token")
        return Result.Success(Pair(fakeUser, fakeTokens))
    }

    override suspend fun logInWithPassword(
        phone: String,
        password: String
    ): Result<Pair<User, AuthTokens>, DataError> {
        val fakeUser = User(id = 1, phoneNumber = phone, isVerified = true)
        val fakeTokens = AuthTokens(accessToken = "fake_access_token", refreshToken = "fake_refresh_token")
        return Result.Success(Pair(fakeUser, fakeTokens))
    }

    override suspend fun logInWithOtp(phone: String): Result<Unit, DataError> {
        return Result.Success(Unit)
    }

    override suspend fun resentOtp(phone: String): Result<Unit, DataError> {
        return Result.Success(Unit)
    }

    override suspend fun resetPassword(phone: String, newPassword: String): Result<Unit, DataError> {
        return Result.Success(Unit)
    }

    override suspend fun logout(): Result<Unit, DataError> {
        return Result.Success(Unit)
    }
}