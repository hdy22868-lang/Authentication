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
import com.example.authentication.core.domain.DataError
import com.example.authentication.core.domain.Result


class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource,
    private val authPreferences: AuthPreferences
) : AuthRepository {
    override suspend fun signUp(
        phone: String,
        password: String,
        name: String
    ): Result<Unit, DataError> {
        return try {
            remoteDataSource.signUp(
                SignUpRequestDto(
                    phoneNumber = phone,
                    password = password,
                    name = name
                )
            )
            Result.Success(Unit)
        }catch (e: Exception){
            Result.Error(DataError.Network.SERVER_ERROR)
        }
    }

    override suspend fun verify(
        phone: String,
        otp: String
    ): Result<Pair<User, AuthTokens>, DataError> {
        return try {
            val response = remoteDataSource.verifyOtp(VerifyOtpRequestDto(phoneNumber = phone,
                otpCode = otp))
            val accessToken = response.accessToken ?:""
            val refreshToken = response.refreshToken ?: ""
            authPreferences.saveTokens(accessToken,refreshToken)
            Result.Success(Pair(response.toUser(),response.toAuthToken()))
        }catch (e: Exception){
            Result.Error(DataError.Network.SERVER_ERROR)
        }
    }

    override suspend fun logInWithPassword(
        phone: String,
        password: String
    ): Result<Pair<User, AuthTokens>, DataError> {
        return try {
            val response = remoteDataSource.loginWithPassword(LoginPasswordRequestDto(phoneNumber = phone, password = password))

            val accessToken = response.accessToken ?: ""
            val refreshToken = response.refreshToken ?: ""
            authPreferences.saveTokens(accessToken, refreshToken)

            Result.Success(Pair(response.toUser(), response.toAuthToken()))
        } catch (e: Exception) {
            Result.Error(DataError.Network.SERVER_ERROR)
        }
    }

    override suspend fun logInWithOtp(phone: String): Result<Unit, DataError> {
        return try {
            remoteDataSource.loginWithOtp(LoginOtpRequestDto(phoneNumber = phone))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Network.SERVER_ERROR)
        }
    }

    override suspend fun resentOtp(phone: String): Result<Unit, DataError> {
        return try {
            remoteDataSource.loginWithOtp(LoginOtpRequestDto(phoneNumber = phone))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Network.SERVER_ERROR)
        }
    }

    override suspend fun logout(): Result<Unit, DataError> {
        return try {
            authPreferences.clearTokens()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun resetPassword(
        phone: String,
        newPassword: String
    ): Result<Unit, DataError> {
        return try {
            remoteDataSource.resetPassword(ResetPasswordRequestDto(phoneNumber = phone, newPassword = newPassword))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Network.SERVER_ERROR)
        }
    }


}