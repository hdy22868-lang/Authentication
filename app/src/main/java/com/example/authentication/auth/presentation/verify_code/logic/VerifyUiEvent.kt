package com.example.authentication.auth.presentation.verify_code.logic

import com.example.authentication.core.component.localization.UiText

sealed interface VerifyUiEvent {
    data object VerifySuccess : VerifyUiEvent
    data class ShowToast(val message: UiText) : VerifyUiEvent
}