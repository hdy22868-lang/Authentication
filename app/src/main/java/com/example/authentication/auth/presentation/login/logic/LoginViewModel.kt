package com.example.authentication.auth.presentation.login.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.auth.domain.use_cases.LogInPasswordUseCase
import com.example.authentication.core.component.localization.toUiText
import com.example.authentication.core.domain.DataError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import com.example.authentication.core.domain.Result
import kotlinx.coroutines.launch

class LoginViewModel(
    private val logInPasswordUseCase: LogInPasswordUseCase
): ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    private val _uiEvent = Channel<LoginEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: LoginAction){
        when(action){
            LoginAction.OnLoginClick -> {
                login()
            }
            is LoginAction.OnPasswordChanged -> {
                {
                    _state.update { it.copy(password = action.password) }
                }
            }
            is LoginAction.OnPhoneNumberChanged -> {
                {
                    _state.update { it.copy(phoneNumber = action.phoneNumber) }
                }
            }
        }
    }
    private fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = logInPasswordUseCase(
                phone = _state.value.phoneNumber,
                password = _state.value.password
            )

            _state.update { it.copy(isLoading = false) }

            when (result) {
                is Result.Success -> {
                    _uiEvent.send(LoginEvent.LoginSuccess)
                }

                is Result.Error -> {
                    if (result.error == DataError.Network.UNAUTHORIZED) {
                        _uiEvent.send(LoginEvent.ShowAccountNotFoundDialog)
                    } else {
                        _state.update { it.copy(error = result.error.toUiText()) }
                    }
                }
            }
        }
    }
}