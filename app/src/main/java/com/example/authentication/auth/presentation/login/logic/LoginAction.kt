package com.example.authentication.auth.presentation.login.logic

sealed interface LoginAction {
    data class OnPhoneNumberChanged(val phoneNumber: String) : LoginAction
    data class OnPasswordChanged(val password: String) : LoginAction
    data class OnCountryCodeChanged(val callingCode: String, val isoCode: String) : LoginAction
    data object OnLoginClick : LoginAction
    data object OnForgetPasswordClick : LoginAction
}