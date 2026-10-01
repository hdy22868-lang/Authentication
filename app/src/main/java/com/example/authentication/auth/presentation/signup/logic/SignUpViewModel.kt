package com.example.authentication.auth.presentation.signup.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.auth.domain.use_cases.SignUpUseCase
import com.example.authentication.core.component.localization.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import com.example.authentication.core.domain.Result
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase
): ViewModel(){
    private val _state = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state.asStateFlow()

    private val _uiEvent = Channel<SignUpUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: SignUpAction) {
        when (action) {
            is SignUpAction.OnNameChanged -> {
                _state.update { it.copy(name = action.name) }
            }
            is SignUpAction.OnPhoneNumberChanged -> {
                _state.update { it.copy(phoneNumber = action.phoneNumber) }
            }
            is SignUpAction.OnPasswordChanged -> {
                _state.update { it.copy(password = action.password) }
            }
            is SignUpAction.OnSignUpClick -> {
                signUp()
            }
        }
    }

    private fun signUp() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = signUpUseCase(
                phone = _state.value.phoneNumber,
                password = _state.value.password,
                name = _state.value.name
            )

            _state.update { it.copy(isLoading = false) }

            when (result) {
                is Result.Success -> {
                    _uiEvent.send(SignUpUiEvent.NavigateToVerify(_state.value.phoneNumber))
                }
                is Result.Error -> {
                    _state.update { it.copy(error = result.error.toUiText()) }
                }
            }
        }
    }
}