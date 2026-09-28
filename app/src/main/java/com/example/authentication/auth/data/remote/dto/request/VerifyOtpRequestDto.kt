package com.example.authentication.auth.data.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpRequestDto(
    val phoneNumber: String,
    val otpCode: String
)