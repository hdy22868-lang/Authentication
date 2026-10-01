package com.example.authentication.auth.presentation.reset_password.logic

import com.example.authentication.core.component.localization.UiText

data class ResetPasswordState(
    val password: String = "",
    val confirmedPassword: String = "",
    val isLoading: Boolean = false,
    val error: UiText? = null
)