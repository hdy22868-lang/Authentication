package com.example.authentication.auth.presentation.verify_code.logic

sealed interface VerifyAction {
    data class OnOtpChanged(val otpCode: String) : VerifyAction
    data object OnVerifyClick : VerifyAction
    data object OnResendOtpClick : VerifyAction
}