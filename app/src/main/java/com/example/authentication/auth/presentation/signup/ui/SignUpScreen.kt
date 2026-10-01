package com.example.authentication.auth.presentation.signup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
import com.example.authentication.auth.presentation.component.AuthBackground
import com.example.authentication.auth.presentation.component.AuthFooter
import com.example.authentication.auth.presentation.component.AuthHeader
import com.example.authentication.auth.presentation.component.AuthTextField
import com.example.authentication.auth.presentation.component.PhoneTextField
import com.example.authentication.auth.presentation.signup.logic.SignUpAction
import com.example.authentication.auth.presentation.signup.logic.SignUpState

@Composable
fun SignUpScreen(
    state: SignUpState,
    onAction: (SignUpAction) -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    AuthBackground {
        Scaffold(
            modifier = modifier
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                },
            containerColor = Color.Transparent,
            bottomBar = {
                AuthFooter(
                    promptText = stringResource(R.string.already_have_account),
                    actionText = stringResource(R.string.sign_in),
                    onActionClick = onLoginClick
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
                    titleRes = R.string.register_title,
                    subtitleRes = R.string.register_subtitle,
                    imageRes = R.drawable.register
                )

                AuthTextField(
                    value = state.fullName,
                    onValueChange = { onAction(SignUpAction.OnFullNameChanged(it)) },
                    placeholder = stringResource(R.string.full_name),
                    leadingIcon = Icons.Default.Person,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                PhoneTextField(
                    label = stringResource(R.string.phone_number),
                    placeholder = stringResource(R.string.enter_phone_number),
                    value = state.phoneNumber,
                    countryCode = state.countryCode,
                    onValueChange = { onAction(SignUpAction.OnPhoneNumberChanged(it)) },
                    onCountryCodeSelected = { country ->
                        onAction(
                            SignUpAction.OnCountryCodeChanged(
                                country.callingCode,
                                country.isoCode
                            )
                        )
                    },
                    error = state.phoneError?.asString(context),
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
                    onValueChange = { onAction(SignUpAction.OnPasswordChanged(it)) },
                    placeholder = stringResource(R.string.enter_your_password),
                    leadingIcon = Icons.Default.Lock,
                    trailingIcon = {
                        val image =
                            if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = null, tint = Color.Gray)
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            onAction(SignUpAction.OnSignUpClick)
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        onAction(SignUpAction.OnSignUpClick)
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
                            text = stringResource(R.string.sign_up),
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}