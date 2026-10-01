package com.example.authentication.auth.presentation.verify_code.logic

import com.example.authentication.core.component.localization.UiText

data class VerifyState(
    val phoneNumber: String = "",
    val otpCode: String = "",
    val isLoading: Boolean = false,
    val error: UiText? = null,
    val resendTimer: Int = 60,
    val isResendEnabled: Boolean = false
)