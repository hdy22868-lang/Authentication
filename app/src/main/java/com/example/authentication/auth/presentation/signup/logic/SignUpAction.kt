package com.example.authentication.auth.presentation.signup.logic

sealed interface SignUpAction{
    data class OnNameChanged(val name: String) : SignUpAction
    data class OnPhoneNumberChanged(val phoneNumber: String) : SignUpAction
    data class OnPasswordChanged(val password: String) : SignUpAction
    data object OnSignUpClick : SignUpAction
}