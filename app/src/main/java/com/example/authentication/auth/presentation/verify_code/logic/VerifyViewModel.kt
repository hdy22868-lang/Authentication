package com.example.authentication.auth.presentation.verify_code.logic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.auth.domain.use_cases.ResendOtpUseCase
import com.example.authentication.auth.domain.use_cases.VerifyUseCase
import com.example.authentication.core.component.localization.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.authentication.core.domain.Result

class VerifyViewModel (
    savedStateHandle: SavedStateHandle,
    private val verifyUseCase: VerifyUseCase,
    private val resendOtpUseCase: ResendOtpUseCase
): ViewModel(){

    private val phoneNumber: String = savedStateHandle.get<String>("phoneNumber") ?: ""

    private val _state = MutableStateFlow(VerifyState(phoneNumber = phoneNumber))
    val state: StateFlow<VerifyState> = _state.asStateFlow()

    private val _uiEvent = Channel<VerifyUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private fun verifyOtp() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val result = verifyUseCase(
                phone = _state.value.phoneNumber,
                otp = _state.value.otpCode
            )

            _state.update { it.copy(isLoading = false) }

            when (result) {
                is Result.Success -> {
                    _uiEvent.send(VerifyUiEvent.VerifySuccess)
                }
                is Result.Error -> {
                    _state.update { it.copy(error = result.error.toUiText()) }
                }
            }
        }
    }

    private fun resendOtp() {
        viewModelScope.launch {
            val result = resendOtpUseCase(_state.value.phoneNumber)

            when (result) {
                is Result.Success -> {
                }
                is Result.Error -> {
                    _state.update { it.copy(error = result.error.toUiText()) }
                }
            }
        }
    }
}