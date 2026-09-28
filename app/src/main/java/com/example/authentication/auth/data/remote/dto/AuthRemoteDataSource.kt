package com.example.authentication.auth.data.remote.dto

import com.example.authentication.auth.data.remote.dto.request.LoginOtpRequestDto
import com.example.authentication.auth.data.remote.dto.request.LoginPasswordRequestDto
import com.example.authentication.auth.data.remote.dto.request.ResetPasswordRequestDto
import com.example.authentication.auth.data.remote.dto.request.SignUpRequestDto
import com.example.authentication.auth.data.remote.dto.request.VerifyOtpRequestDto
import com.example.authentication.auth.data.remote.dto.response.AuthResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun signUp(requestDto: SignUpRequestDto): AuthResponseDto{
        return httpClient.post("/api/auth/sign_up"){
            contentType(ContentType.Application.Json)
            setBody(requestDto)
        }.body()
    }
    suspend fun loginWithPassword(requestDto: LoginPasswordRequestDto): AuthResponseDto{
        return httpClient.post("/api/auth/login-password"){
            contentType(ContentType.Application.Json)
            setBody(requestDto)
        }.body()
    }

    suspend fun loginWithOtp(requestDto: LoginOtpRequestDto): Unit{
        return httpClient.post("/api/auth/login-otp"){
            contentType(ContentType.Application.Json)
            setBody(requestDto)
        }.body()
    }

    suspend fun verifyOtp(request: VerifyOtpRequestDto): AuthResponseDto {
        return httpClient.post("/api/auth/verify-otp") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
    suspend fun resetPassword(request: ResetPasswordRequestDto): Unit {
        httpClient.post("/api/auth/reset-password") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }
}