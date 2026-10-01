package com.example.authentication.auth.presentation.signup.logic

import com.example.authentication.core.component.localization.UiText

data class SignUpState(
    val fullName: String = "",
    val phoneNumber: String = "",
    val countryCode: String = "+964",
    val password: String = "",
    val phoneError: UiText? = null,
    val isLoading: Boolean = false
)