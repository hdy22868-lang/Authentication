package com.example.authentication.auth.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val userId: String? = null,
    val name: String? = null,
    val phoneNumber: String? = null
)