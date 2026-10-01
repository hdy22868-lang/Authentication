package com.example.authentication.auth.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
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
                onNavigateToSignUp = {
                    navController.navigate(AuthRoute.SignUp)
                },
                onLoginSuccess = {
                }
            )
        }

        composable<AuthRoute.SignUp> {
            SignUpScreenRoot(
                viewModel = koinViewModel(),
                onNavigateToVerify = { phone ->
                    // الانتقال لشاشة الـ Verify وتمرير رقم الهاتف معها
                    navController.navigate(AuthRoute.Verify(phoneNumber = phone))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // 3. شاشة التحقق (Verify OTP)
        composable<AuthRoute.Verify> { backStackEntry ->
            val verifyRoute = backStackEntry.toRoute<AuthRoute.Verify>()
            VerifyScreenRoot(
                viewModel = koinViewModel(), // Koin سيتكفل بالـ SavedStateHandle وجلب الـ phoneNumber تلقائياً!
                onVerifySuccess = {
                    // هنا حسب الـ Flow: إذا كان جاي من تسجيل جديد يروح للـ Home، أو للـ ResetPassword إذا كان ناسي الباسورد
                    // كمثال بسيط سنحوله للـ Home أو شاشة جديدة
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // 4. شاشة إعادة تعيين كلمة المرور
        composable<AuthRoute.ResetPassword> { backStackEntry ->
            val resetRoute = backStackEntry.toRoute<AuthRoute.ResetPassword>()
            ResetPasswordScreenRoot(
                viewModel = koinViewModel(),
                onResetSuccess = {
                    // العودة لشاشة اللوجن بعد نجاح تغيير الباسورد
                    navController.navigate(AuthRoute.Login) {
                        popUpTo(AuthRoute.Login) { inclusive = true }
                    }
                }
            )
        }
    }
}