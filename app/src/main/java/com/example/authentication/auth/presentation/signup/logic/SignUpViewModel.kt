package com.example.authentication.auth.presentation.signup.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.R
import com.example.authentication.auth.domain.use_cases.SignUpUseCase
import com.example.authentication.core.component.localization.UiText
import com.example.authentication.core.component.localization.toUiText
import com.example.authentication.core.component.phoneNumber.PhoneNumberValidator
import com.example.authentication.core.domain.DataError
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
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state.asStateFlow()

    private val _uiEvent = Channel<SignUpUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: SignUpAction) {
        when (action) {
            is SignUpAction.OnFullNameChanged -> _state.update { it.copy(fullName = action.name) }
            is SignUpAction.OnPhoneNumberChanged -> _state.update { it.copy(phoneNumber = action.number, phoneError = null) }
            is SignUpAction.OnPasswordChanged -> _state.update { it.copy(password = action.pass) }
            is SignUpAction.OnCountryCodeChanged -> _state.update {
                it.copy(countryCode = action.callingCode, isoCode = action.isoCode, phoneError = null)
            }
            is SignUpAction.OnSignUpClick -> signUp()
        }
    }

    private fun signUp() {
        val current = _state.value
        val phone = phoneNumberValidator.formatToE164(current.phoneNumber, current.isoCode)
        if (phone == null) {
            _state.update { it.copy(phoneError = UiText.StringResource(R.string.error_invalid_phone)) }
            return
        }
        if (current.password.isBlank()) {
            viewModelScope.launch {
                _uiEvent.send(SignUpUiEvent.ShowToast(UiText.StringResource(R.string.enter_your_password)))
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = signUpUseCase(phone = phone, password = current.password, name = current.fullName)
            _state.update { it.copy(isLoading = false) }

            when (result) {
                is Result.Success -> _uiEvent.send(SignUpUiEvent.NavigateToVerify(phone))
                is Result.Error -> {
                    if (result.error == DataError.Network.CONFLICT) {
                        _uiEvent.send(SignUpUiEvent.ShowUserAlreadyExistsDialog)
                    } else {
                        _uiEvent.send(SignUpUiEvent.ShowToast(result.error.toUiText()))
                    }
                }
            }
        }
    }
}