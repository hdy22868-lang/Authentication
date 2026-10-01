package com.example.authentication.auth.presentation.signup.ui

import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.authentication.R
import com.example.authentication.auth.presentation.signup.logic.SignUpUiEvent
import com.example.authentication.auth.presentation.signup.logic.SignUpViewModel

@Composable
fun SignUpScreenRoot(
    viewModel: SignUpViewModel,
    onNavigateToLogin: () -> Unit,
    onNavigateToOtp: (String) -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    var showUserExistsDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is SignUpUiEvent.NavigateToVerify -> {
                    onNavigateToOtp(event.phoneNumber)
                }
                is SignUpUiEvent.ShowUserAlreadyExistsDialog -> {
                    showUserExistsDialog = true
                }

                is SignUpUiEvent.ShowToast ->{
                    Toast.makeText(
                        context,
                        event.message.asString(context),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    SignUpScreen(
        state = state,
        onAction = viewModel::onAction,
        onLoginClick = onNavigateToLogin
    )

    if (showUserExistsDialog) {
        AlertDialog(
            onDismissRequest = { showUserExistsDialog = false },
            title = { Text(stringResource(R.string.account_exists_title)) },
            text = { Text(stringResource(R.string.account_exists_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showUserExistsDialog = false
                        onNavigateToLogin()
                    }
                ) {
                    Text(stringResource(R.string.go_to_login))
                }
            },
            dismissButton = {
                TextButton(onClick = { showUserExistsDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}