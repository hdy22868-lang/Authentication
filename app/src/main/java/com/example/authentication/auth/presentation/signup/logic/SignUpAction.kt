package com.example.authentication.auth.presentation.signup.logic

sealed interface SignUpAction{
    data class OnFullNameChanged(val name: String) : SignUpAction
    data class OnPhoneNumberChanged(val number: String) : SignUpAction
    data class OnCountryCodeChanged(val callingCode: String, val isoCode: String) : SignUpAction
    data class OnPasswordChanged(val pass: String) : SignUpAction
    object OnSignUpClick : SignUpAction
}