package com.example.authentication.auth.presentation.verify_code.ui

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.authentication.auth.presentation.verify_code.logic.VerifyUiEvent
import com.example.authentication.auth.presentation.verify_code.logic.VerifyViewModel

@Composable
fun VerifyScreenRoot(
    viewModel: VerifyViewModel,
    onNavigateToHome: () -> Unit // أو OnNavigateToUpdatePassword
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is VerifyUiEvent.VerifySuccess -> {
                    onNavigateToHome()
                }
                is VerifyUiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.message.asString(context),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    VerifyScreen(
        state = state,
        onAction = viewModel::onAction
    )
}