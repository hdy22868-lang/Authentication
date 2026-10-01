package com.example.authentication.auth.presentation.signup.logic

import com.example.authentication.core.component.localization.UiText

sealed interface SignUpUiEvent{
    data class NavigateToVerify(val phoneNumber: String) : SignUpUiEvent
    data class ShowToast(val message: UiText) : SignUpUiEvent
}