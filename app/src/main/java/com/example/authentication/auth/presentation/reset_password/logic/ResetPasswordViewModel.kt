package com.example.authentication.auth.presentation.reset_password.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.SavedStateHandle
import com.example.authentication.R
import com.example.authentication.core.domain.Result
import com.example.authentication.auth.domain.use_cases.ResetPasswordUseCase
import com.example.authentication.core.component.localization.UiText
import com.example.authentication.core.component.localization.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    savedStateHandle: SavedStateHandle,
    private val resetPasswordUseCase: ResetPasswordUseCase
): ViewModel() {
    private val phoneNumber: String = savedStateHandle.get<String>("phoneNumber") ?: ""

    private val _state = MutableStateFlow(ResetPasswordState())
    val state: StateFlow<ResetPasswordState> = _state.asStateFlow()

    private val _uiEvent = Channel<ResetPasswordUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: ResetPasswordAction) {
        when (action) {
            is ResetPasswordAction.OnPasswordChanged -> {
                _state.update { it.copy(password = action.password) }
            }
            is ResetPasswordAction.OnConfirmedPasswordChanged -> {
                _state.update { it.copy(confirmedPassword = action.password) }
            }
            is ResetPasswordAction.OnSubmitClick -> {
                resetPassword()
            }
        }
    }
    private fun resetPassword() {
        val currentState = _state.value

        if (currentState.password != currentState.confirmedPassword) {
            _state.update { it.copy(error = UiText.StringResource(R.string.not_match)) }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = resetPasswordUseCase(
                phone = phoneNumber,
                newPassword = currentState.password
            )

            _state.update { it.copy(isLoading = false) }

            when (result) {
                is Result.Success -> {
                    _uiEvent.send(ResetPasswordUiEvent.ResetSuccess)
                }
                is Result.Error -> {
                    _state.update { it.copy(error = result.error.toUiText()) }
                }
            }
        }
    }
}