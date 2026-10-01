package com.example.authentication.auth.presentation.reset_password.logic

sealed interface ResetPasswordAction {
    data class OnPasswordChanged(val password: String) : ResetPasswordAction
    data class OnConfirmedPasswordChanged(val password: String) : ResetPasswordAction
    data object OnSubmitClick : ResetPasswordAction
}