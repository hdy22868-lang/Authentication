package com.example.authentication.auth.data.mapper


import com.example.authentication.auth.data.remote.dto.response.AuthResponseDto
import com.example.authentication.auth.domain.model.AuthTokens
import com.example.authentication.auth.domain.model.User

fun AuthResponseDto.toAuthToken(): AuthTokens {
    return AuthTokens(
        accessToken = accessToken ?: "",
        refreshToken = refreshToken ?: ""
    )
}

fun AuthResponseDto.toUser(): User {
    return User(
        id = userId?.toIntOrNull() ?: 0, // أو إذا كان id في الـ User عبارة عن String حسب ما صممتيه
        phoneNumber = phoneNumber ?: "",
        isVerified = true
    )
}