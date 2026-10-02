package com.example.authentication.auth.data.repository

import com.example.authentication.auth.data.local.AuthPreferences
import com.example.authentication.auth.data.mapper.toAuthToken
import com.example.authentication.auth.data.mapper.toUser
import com.example.authentication.auth.data.remote.dto.AuthRemoteDataSource
import com.example.authentication.auth.data.remote.dto.request.LoginOtpRequestDto
import com.example.authentication.auth.data.remote.dto.request.LoginPasswordRequestDto
import com.example.authentication.auth.data.remote.dto.request.ResetPasswordRequestDto
import com.example.authentication.auth.data.remote.dto.request.SignUpRequestDto
import com.example.authentication.auth.data.remote.dto.request.VerifyOtpRequestDto
import com.example.authentication.auth.domain.model.AuthTokens
import com.example.authentication.auth.domain.model.User
import com.example.authentication.auth.domain.repository.AuthRepository
import com.example.authentication.core.data.networking.clearBearerTokens
import com.example.authentication.core.data.networking.safeCall
import com.example.authentication.core.domain.DataError
import com.example.authentication.core.domain.Result
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import kotlinx.coroutines.flow.first
import kotlinx.serialization.SerializationException

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val remoteDataSource: AuthRemoteDataSource,
    private val authPreferences: AuthPreferences
) : AuthRepository {

    override suspend fun signUp(
        phone: String,
        password: String,
        name: String
    ): Result<Unit, DataError> {
        return safeCall {
            remoteDataSource.signUp(
                SignUpRequestDto(
                    phoneNumber = phone,
                    password = password,
                    name = name
                )
            )
            Unit
        }
    }

    override suspend fun verify(
        phone: String,
        otp: String
    ): Result<Pair<User, AuthTokens>, DataError> {
        return safeCall {
            val response = remoteDataSource.verifyOtp(
                VerifyOtpRequestDto(phoneNumber = phone, otpCode = otp)
            )
            val accessToken = response.accessToken
            val refreshToken = response.refreshToken
            if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) {
                throw SerializationException("Missing tokens")
            }
            authPreferences.saveTokens(accessToken, refreshToken)
            httpClient.clearBearerTokens()
            Pair(response.toUser(), response.toAuthToken())
        }
    }

    override suspend fun logInWithPassword(
        phone: String,
        password: String
    ): Result<Pair<User, AuthTokens>, DataError> {
        return safeCall {
            val response = remoteDataSource.loginWithPassword(
                LoginPasswordRequestDto(phoneNumber = phone, password = password)
            )

            val accessToken = response.accessToken
            val refreshToken = response.refreshToken
            if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) {
                throw SerializationException("Missing tokens")
            }
            authPreferences.saveTokens(accessToken, refreshToken)
            httpClient.clearBearerTokens()
            Pair(response.toUser(), response.toAuthToken())
        }
    }

    override suspend fun logInWithOtp(phone: String): Result<Unit, DataError> {
        return safeCall {
            remoteDataSource.loginWithOtp(LoginOtpRequestDto(phoneNumber = phone))
        }
    }

    override suspend fun resentOtp(phone: String): Result<Unit, DataError> {
        return safeCall {
            remoteDataSource.loginWithOtp(LoginOtpRequestDto(phoneNumber = phone))
        }
    }

    override suspend fun logout(): Result<Unit, DataError> {
        return safeCall {
            authPreferences.clearTokens()
            httpClient.clearBearerTokens()
        }
    }

    override suspend fun resetPassword(
        phone: String,
        newPassword: String
    ): Result<Unit, DataError> {
        return safeCall {
            remoteDataSource.resetPassword(
                ResetPasswordRequestDto(phoneNumber = phone, newPassword = newPassword)
            )
        }
    }
    override suspend fun isLoggedIn(): Boolean {
        return !authPreferences.accessTokenFlow.first().isNullOrBlank()
    }
}