package com.example.authentication.auth.presentation.reset_password.ui

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.authentication.auth.presentation.reset_password.logic.ResetPasswordUiEvent
import com.example.authentication.auth.presentation.reset_password.logic.ResetPasswordViewModel

@Composable
fun ResetPasswordScreenRoot(
    viewModel: ResetPasswordViewModel,
    onNavigateToHome: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ResetPasswordUiEvent.ResetSuccess -> {
                    onNavigateToHome()
                }
                is ResetPasswordUiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.message.asString(context),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    ResetPasswordScreen(
        state = state,
        onAction = viewModel::onAction
    )
}