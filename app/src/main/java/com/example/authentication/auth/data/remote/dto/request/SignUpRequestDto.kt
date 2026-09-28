package com.example.authentication.auth.data.remote.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequestDto(
    val phoneNumber: String,
    val password: String,
    val name: String? = null
)