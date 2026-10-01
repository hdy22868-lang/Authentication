package com.example.authentication.auth.presentation.verify_code.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.authentication.R
import com.example.authentication.auth.presentation.component.AuthBackground
import com.example.authentication.auth.presentation.component.OtpInputField
import com.example.authentication.auth.presentation.verify_code.logic.VerifyAction
import com.example.authentication.auth.presentation.verify_code.logic.VerifyState

@Composable
fun VerifyScreen(
    state: VerifyState,
    onAction: (VerifyAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    AuthBackground {
    Scaffold(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(scrollState)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Spacer(modifier = Modifier.height(40.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(id = R.string.verify_title),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.verify_subtitle, state.phoneNumber),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }


            OtpInputField(
                otpText = state.otpCode,
                otpLength = 6,
                onOtpChange = { onAction(VerifyAction.OnOtpChanged(it)) }
            )

            Button(
                onClick = {
                    focusManager.clearFocus()
                    onAction(VerifyAction.OnVerifyClick)
                },
                enabled = !state.isLoading && state.otpCode.length == 6,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = stringResource(R.string.verify),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { onAction(VerifyAction.OnResendOtpClick) },
                    enabled = state.isResendEnabled && !state.isLoading
                ) {
                    Text(
                        text = if (state.isResendEnabled) {
                            stringResource(R.string.resend_code)
                        } else {
                            stringResource(R.string.resend_code_in, state.resendTimer)
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (state.isResendEnabled) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    )
                }
            }


            if (state.error != null) {
                Text(
                    text = state.error.asString(context),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }

}
}