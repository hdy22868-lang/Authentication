package com.example.authentication.auth.presentation.login.logic

import com.example.authentication.core.component.localization.UiText


interface LoginEvent {
    data object LoginSuccess : LoginEvent
    data class ShowToast(val massage: UiText) : LoginEvent
    data object ShowAccountNotFoundDialog : LoginEvent
}