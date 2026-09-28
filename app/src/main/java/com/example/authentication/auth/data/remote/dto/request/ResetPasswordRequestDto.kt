package com.example.authentication.auth.data.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequestDto(
    val phoneNumber: String,
    val newPassword: String
)