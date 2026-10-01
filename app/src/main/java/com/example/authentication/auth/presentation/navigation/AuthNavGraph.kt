package com.example.authentication.auth.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import org.koin.androidx.compose.koinViewModel
import com.example.authentication.auth.presentation.component.AuthBackground
import com.example.authentication.auth.presentation.login.ui.LoginScreenRoot
import com.example.authentication.auth.presentation.login.logic.LoginViewModel
import com.example.authentication.auth.presentation.signup.ui.SignUpScreenRoot
import com.example.authentication.auth.presentation.signup.logic.SignUpViewModel
import com.example.authentication.auth.presentation.verify_code.ui.VerifyScreenRoot
import com.example.authentication.auth.presentation.verify_code.logic.VerifyViewModel
import com.example.authentication.auth.presentation.reset_password.ui.ResetPasswordScreenRoot
import com.example.authentication.auth.presentation.reset_password.logic.ResetPasswordViewModel
import com.example.authentication.auth.presentation.welcome.ui.WelcomeScreen
import com.example.authentication.core.component.navigateAndClearSafe
import com.example.authentication.core.component.navigateSafe
import com.example.authentication.home.HomeScreenRoot
import com.example.authentication.home.HomeViewModel

@Composable
fun AuthNavGraph(
    navController: NavHostController,
    startDestination: AuthRoute = AuthRoute.Welcome) {
    AuthBackground {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {

            composable<AuthRoute.Welcome> {
                WelcomeScreen(
                    onSignUpClick = { navController.navigateSafe(AuthRoute.SignUp) },
                    onSignInClick = { navController.navigateSafe(AuthRoute.Login) },
                )
            }

            composable<AuthRoute.Login> {
                val viewModel = koinViewModel<LoginViewModel>()
                LoginScreenRoot(
                    viewModel = viewModel,
                    onLoginSuccess = {

                        navController.navigateAndClearSafe<AuthRoute.Welcome>(
                            route = AuthRoute.Home
                        )
                    },
                    onNavigateToSignUp = { navController.navigateSafe(AuthRoute.SignUp) },
                    onNavigateToVerify = { phone ->
                        navController.navigateSafe(
                            AuthRoute.Verify(phoneNumber = phone, isResetFlow = true)
                        )
                    }
                )
            }

            composable<AuthRoute.SignUp> {
                val viewModel = koinViewModel<SignUpViewModel>()
                SignUpScreenRoot(
                    viewModel = viewModel,
                    onNavigateToLogin = {
                        navController.navigateAndClearSafe<AuthRoute.SignUp>(
                            route = AuthRoute.Login
                        )
                    },
                    onNavigateToOtp = { phone ->
                        navController.navigateSafe(
                            AuthRoute.Verify(phoneNumber = phone, isResetFlow = false)
                        )
                    }
                )
            }

            composable<AuthRoute.Verify> { backStackEntry ->
                val args = backStackEntry.toRoute<AuthRoute.Verify>()
                val viewModel = koinViewModel<VerifyViewModel>()

                VerifyScreenRoot(
                    viewModel = viewModel,
                    onNavigateToHome = {
                        if (args.isResetFlow) {
                            navController.navigateAndClearSafe<AuthRoute.Verify>(
                                route = AuthRoute.ResetPassword(phoneNumber = args.phoneNumber)
                            )
                        } else {
                            navController.navigateAndClearSafe<AuthRoute.Welcome>(
                                route = AuthRoute.Home
                            )
                        }
                    }
                )
            }

            composable<AuthRoute.ResetPassword> { backStackEntry ->
                val args = backStackEntry.toRoute<AuthRoute.ResetPassword>()
                val viewModel = koinViewModel<ResetPasswordViewModel>()

                ResetPasswordScreenRoot(
                    viewModel = viewModel,
                    onNavigateToHome = {
                        navController.navigateAndClearSafe<AuthRoute.Welcome>(
                            route = AuthRoute.Home
                        )
                    }
                )
            }


            composable<AuthRoute.Home> {
                val viewModel = koinViewModel<HomeViewModel>()
                HomeScreenRoot(
                    viewModel = viewModel,
                    onLoggedOut = {
                        navController.navigateAndClearSafe<AuthRoute.Home>(
                            route = AuthRoute.Welcome
                        )
                    }
                )
            }
        }
    }
}