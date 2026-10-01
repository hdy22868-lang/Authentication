package com.example.authentication.auth.presentation.login.logic

import com.example.authentication.core.component.localization.UiText

data class LoginState(
    val phoneNumber: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val phoneError: UiText? = null,
    val countryCode: String = "+964",
    val isoCode: String = "IQ",
)