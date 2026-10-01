package com.example.authentication.auth.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AuthRoute {
    @Serializable
    data object Login : AuthRoute

    @Serializable
    data object SignUp : AuthRoute

    @Serializable
    data class Verify(val phoneNumber: String, val isResetFlow: Boolean = false) : AuthRoute

    @Serializable
    data class ResetPassword(val phoneNumber: String) : AuthRoute
    @Serializable
    data object Home : AuthRoute
}