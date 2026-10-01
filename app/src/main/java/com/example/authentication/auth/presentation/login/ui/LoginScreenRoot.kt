package com.example.authentication.auth.presentation.login.ui

import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.authentication.R
import com.example.authentication.auth.presentation.login.logic.LoginAction
import com.example.authentication.auth.presentation.login.logic.LoginEvent
import com.example.authentication.auth.presentation.login.logic.LoginViewModel

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel,
    onNavigateToSignUp: () -> Unit,
    onNavigateToVerify: (String) -> Unit,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showAccountNotFoundDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                LoginEvent.LoginSuccess -> onLoginSuccess()
                is LoginEvent.NavigateToVerify -> onNavigateToVerify(event.phoneNumber)
                is LoginEvent.ShowToast -> Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_SHORT
                ).show()
                LoginEvent.ShowAccountNotFoundDialog -> showAccountNotFoundDialog = true
            }
        }
    }

    LoginScreen(
        state = state,
        onAction = viewModel::onAction,
        onSignUpClick = onNavigateToSignUp,
        onForgetPasswordClick = { viewModel.onAction(LoginAction.OnForgetPasswordClick) },
        modifier = modifier
    )

    if (showAccountNotFoundDialog) {
        AlertDialog(
            onDismissRequest = { showAccountNotFoundDialog = false },
            title = { Text(stringResource(R.string.account_not_found_title)) },
            text = { Text(stringResource(R.string.account_not_found_message)) },
            confirmButton = {
                TextButton(onClick = {
                    showAccountNotFoundDialog = false
                    onNavigateToSignUp()
                }) { Text(stringResource(R.string.sign_up)) }
            },
            dismissButton = {
                TextButton(onClick = { showAccountNotFoundDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}