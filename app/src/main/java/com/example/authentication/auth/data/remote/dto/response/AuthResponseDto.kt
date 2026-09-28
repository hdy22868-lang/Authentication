package com.example.authentication.auth.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    val accessToken: String?,
    val refreshToken: String?,
    val userId: String?,
    val name: String?,
    val phoneNumber: String?
)