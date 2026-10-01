package com.example.authentication.auth.presentation.login.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.R
import com.example.authentication.auth.domain.use_cases.LogInOtpUseCase
import com.example.authentication.auth.domain.use_cases.LogInPasswordUseCase
import com.example.authentication.core.component.localization.UiText
import com.example.authentication.core.component.localization.toUiText
import com.example.authentication.core.component.phoneNumber.PhoneNumberValidator
import com.example.authentication.core.domain.DataError
import com.example.authentication.core.domain.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val logInPasswordUseCase: LogInPasswordUseCase,
    private val logInOtpUseCase: LogInOtpUseCase,
    private val phoneNumberValidator: PhoneNumberValidator
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _uiEvent = Channel<LoginEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnPhoneNumberChanged -> {
                _state.update { it.copy(phoneNumber = action.phoneNumber, phoneError = null) }
            }
            is LoginAction.OnPasswordChanged -> {
                _state.update { it.copy(password = action.password) }
            }
            is LoginAction.OnCountryCodeChanged -> {
                _state.update {
                    it.copy(
                        countryCode = action.callingCode,
                        isoCode = action.isoCode,
                        phoneError = null
                    )
                }
            }
            LoginAction.OnLoginClick -> login()
            LoginAction.OnForgetPasswordClick -> forgetPassword()
        }
    }

    private fun validatedPhone(): String? {
        val current = _state.value
        val e164 = phoneNumberValidator.formatToE164(current.phoneNumber, current.isoCode)
        if (e164 == null) {
            _state.update {
                it.copy(phoneError = UiText.StringResource(R.string.error_invalid_phone))
            }
        }
        return e164
    }

    private fun login() {
        val phone = validatedPhone() ?: return
        val password = _state.value.password

        if (password.isBlank()) {
            viewModelScope.launch {
                _uiEvent.send(
                    LoginEvent.ShowToast(UiText.StringResource(R.string.enter_your_password))
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = logInPasswordUseCase(phone = phone, password = password)
            _state.update { it.copy(isLoading = false) }

            when (result) {
                is Result.Success -> _uiEvent.send(LoginEvent.LoginSuccess)
                is Result.Error -> {
                    if (result.error == DataError.Network.UNAUTHORIZED) {
                        _uiEvent.send(LoginEvent.ShowAccountNotFoundDialog)
                    } else {
                        _uiEvent.send(LoginEvent.ShowToast(result.error.toUiText()))
                    }
                }
            }
        }
    }

    private fun forgetPassword() {
        val phone = validatedPhone() ?: return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = logInOtpUseCase(phone)
            _state.update { it.copy(isLoading = false) }

            when (result) {
                is Result.Success -> _uiEvent.send(LoginEvent.NavigateToVerify(phone))
                is Result.Error -> _uiEvent.send(LoginEvent.ShowToast(result.error.toUiText()))
            }
        }
    }
}