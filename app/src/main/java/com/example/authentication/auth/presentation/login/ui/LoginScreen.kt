package com.example.authentication.auth.presentation.login.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalAutofillManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.authentication.R
import com.example.authentication.auth.presentation.component.AuthFooter
import com.example.authentication.auth.presentation.component.AuthHeader
import com.example.authentication.auth.presentation.component.AuthTextField
import com.example.authentication.auth.presentation.component.PhoneTextField
import com.example.authentication.auth.presentation.login.logic.LoginAction
import com.example.authentication.auth.presentation.login.logic.LoginState

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    onSignUpClick: () -> Unit,
    onForgetPasswordClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val autofillManager = LocalAutofillManager.current
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            },
        containerColor = Color.Transparent,
        bottomBar = {
            AuthFooter(
                promptText = stringResource(R.string.don_t_have_an_account),
                actionText = stringResource(R.string.sign_up),
                onActionClick = onSignUpClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(scrollState)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AuthHeader(
                titleRes = R.string.login_title,
                subtitleRes = R.string.login_subtitle,
                imageRes = R.drawable.login
            )

            PhoneTextField(
                label = stringResource(R.string.phone_number),
                placeholder = stringResource(R.string.enter_phone_number),
                value = state.phoneNumber,
                countryCode = state.countryCode,
                onValueChange = { onAction(LoginAction.OnPhoneNumberChanged(it)) },
                onCountryCodeSelected = { country ->
                    onAction(LoginAction.OnCountryCodeChanged(country.callingCode, country.isoCode))
                },
                error = state.error?.asString(context),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                contentType = ContentType.PhoneNumber
            )


            AuthTextField(
                value = state.password,
                onValueChange = { onAction(LoginAction.OnPasswordChanged(it)) },
                placeholder = stringResource(R.string.enter_your_password),
                leadingIcon = Icons.Default.Lock,
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = null, tint = Color.Gray)
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                TextButton(onClick = onForgetPasswordClick) {
                    Text(
                        text = stringResource(R.string.forget_password),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Button(
                onClick = {
                    focusManager.clearFocus()
                    autofillManager?.commit()
                    onAction(LoginAction.OnLoginClick)
                },
                enabled = !state.isLoading,
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
                        text = stringResource(R.string.sign_in),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}



















