package com.example.authentication.auth.presentation.signup.logic

import com.example.authentication.core.component.localization.UiText

data class SignUpState(
    val name : String = "",
    val password : String = "",
    val phoneNumber : String = "",
    val error: UiText? = null,
    val isLoading : Boolean = false
)