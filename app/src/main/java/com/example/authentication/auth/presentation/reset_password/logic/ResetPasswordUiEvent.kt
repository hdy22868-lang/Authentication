package com.example.authentication.auth.presentation.reset_password.logic

import com.example.authentication.core.component.localization.UiText

sealed interface ResetPasswordUiEvent {
    data object ResetSuccess : ResetPasswordUiEvent
    data class ShowToast(val message: UiText) : ResetPasswordUiEvent
}