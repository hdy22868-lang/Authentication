package com.example.authentication.auth.presentation.login.logic

import com.example.authentication.core.component.localization.UiText


sealed interface LoginEvent {
    data object LoginSuccess : LoginEvent
    data class ShowToast(val message: UiText) : LoginEvent
    data object ShowAccountNotFoundDialog : LoginEvent
    data class NavigateToVerify(val phoneNumber: String) : LoginEvent
}