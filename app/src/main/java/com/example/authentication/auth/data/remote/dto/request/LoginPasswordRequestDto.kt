package com.example.authentication.auth.data.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginPasswordRequestDto(
    val phoneNumber: String,
    val password: String
)