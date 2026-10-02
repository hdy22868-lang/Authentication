package com.example.authentication.auth.data.remote.dto

import com.example.authentication.auth.data.remote.dto.request.LoginOtpRequestDto
import com.example.authentication.auth.data.remote.dto.request.LoginPasswordRequestDto
import com.example.authentication.auth.data.remote.dto.request.ResetPasswordRequestDto
import com.example.authentication.auth.data.remote.dto.request.SignUpRequestDto
import com.example.authentication.auth.data.remote.dto.request.VerifyOtpRequestDto
import com.example.authentication.auth.data.remote.dto.response.AuthResponseDto
import com.example.authentication.core.data.networking.ApiConfig
import com.example.authentication.core.data.networking.ensureSuccess
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
        return httpClient.post(ApiConfig.SIGN_UP){
            contentType(ContentType.Application.Json)
            setBody(requestDto)
        }.ensureSuccess().body()
    }
    suspend fun loginWithPassword(requestDto: LoginPasswordRequestDto): AuthResponseDto{
        return httpClient.post(ApiConfig.LOGIN_PASSWORD){
            contentType(ContentType.Application.Json)
            setBody(requestDto)
        }.ensureSuccess().body()
    }

    suspend fun loginWithOtp(requestDto: LoginOtpRequestDto): Unit{
        return httpClient.post(ApiConfig.LOGIN_OTP){
            contentType(ContentType.Application.Json)
            setBody(requestDto)
        }.ensureSuccess().body()
    }

    suspend fun verifyOtp(request: VerifyOtpRequestDto): AuthResponseDto {
        return httpClient.post(ApiConfig.VERIFY_OTP) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.ensureSuccess().body()
    }
    suspend fun resetPassword(request: ResetPasswordRequestDto): Unit {
        httpClient.post(ApiConfig.RESET_PASSWORD) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.ensureSuccess()
    }
}