package com.example.authentication.auth.presentation.signup.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.auth.domain.use_cases.SignUpUseCase
import com.example.authentication.core.component.localization.UiText
import com.example.authentication.core.component.localization.toUiText
import com.example.authentication.core.component.phoneNumber.PhoneNumberValidator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import com.example.authentication.core.domain.Result
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val phoneNumberValidator: PhoneNumberValidator
): ViewModel(){

    private val _state = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state.asStateFlow()

    private val _uiEvent = Channel<SignUpUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: SignUpAction) {
        when (action) {
            is SignUpAction.OnFullNameChanged -> {
                _state.update { it.copy(fullName = action.name) }
            }
            is SignUpAction.OnPhoneNumberChanged -> {
                _state.update { it.copy(phoneNumber = action.number) }
            }
            is SignUpAction.OnPasswordChanged -> {
                _state.update { it.copy(password = action.pass) }
            }
            is SignUpAction.OnSignUpClick -> {
                signUp()
            }
            is SignUpAction.OnCountryCodeChanged -> {
                _state.update { it.copy(
                    countryCode = action.callingCode,
                    isoCode = action.isoCode) }
            }
        }
    }

    private fun validatedPhone(): String? {
        val current = _state.value
        val e164 = phoneNumberValidator.formatToE164(current.phoneNumber, current.isoCode)
        if (e164 == null) {
            _state.update {
                it.copy(phoneError = UiText.StringResource(com.example.authentication.R.string.error_invalid_phone))
            }
        }
        return e164
    }

    private fun signUp() {
        val validPhone = validatedPhone() ?: return
        viewModelScope.launch {

            _state.update { it.copy(isLoading = true) }

            val result = signUpUseCase(
                phone = validPhone,
                password = _state.value.password,
                name = _state.value.fullName
            )

            _state.update { it.copy(isLoading = false) }

            when (result) {
                is Result.Success -> {
                    _uiEvent.send(SignUpUiEvent.NavigateToVerify(validPhone))
                }
                is Result.Error -> {
                    _state.update { it.copy(phoneError = result.error.toUiText()) }
                }
            }
        }
    }
}