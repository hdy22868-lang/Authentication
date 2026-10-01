package com.example.authentication.auth.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.authentication.auth.presentation.login.ui.LoginScreenRoot
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AuthRoute.Login
    ) {
        composable<AuthRoute.Login> {
            LoginScreenRoot(
                viewModel = koinViewModel(),
                onNavigateToSignUp = { navController.navigate(AuthRoute.SignUp) },
                onNavigateToVerify = { phone ->
                    navController.navigate(AuthRoute.Verify(phone, isResetFlow = true))
                },
                onLoginSuccess = {
                    navController.navigate(AuthRoute.Home) {
                        popUpTo(AuthRoute.Login) { inclusive = true }
                    }
                }
            )
        }

        composable<AuthRoute.SignUp> { Placeholder("SignUp") }
        composable<AuthRoute.Verify> { Placeholder("Verify") }
        composable<AuthRoute.ResetPassword> { Placeholder("ResetPassword") }
        composable<AuthRoute.Home> { Placeholder("Home") }
    }
}

@Composable
private fun Placeholder(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(name)
    }
}