package com.example.authentication.auth.presentation.verify_code.logic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.R
import com.example.authentication.auth.domain.use_cases.ResendOtpUseCase
import com.example.authentication.auth.domain.use_cases.VerifyUseCase
import com.example.authentication.core.component.localization.UiText
import com.example.authentication.core.component.localization.toUiText
import com.example.authentication.core.domain.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VerifyViewModel(
    savedStateHandle: SavedStateHandle,
    private val verifyUseCase: VerifyUseCase,
    private val resendOtpUseCase: ResendOtpUseCase
) : ViewModel() {

    private val phoneNumber: String = savedStateHandle.get<String>("phoneNumber") ?: ""

    private val _state = MutableStateFlow(VerifyState(phoneNumber = phoneNumber))
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<VerifyUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        startResendTimer()
    }

    fun onAction(action: VerifyAction) {
        when (action) {
            is VerifyAction.OnOtpChanged -> {
                _state.update { it.copy(otpCode = action.otpCode, error = null) }
            }
            VerifyAction.OnVerifyClick -> {
                verify()
            }
            VerifyAction.OnResendOtpClick -> {
                resendOtp()
            }
        }
    }

    private fun verify() {
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
            _state.update { it.copy(isResendEnabled = false, resendTimer = 60, error = null) }
            startResendTimer()

            val result = resendOtpUseCase(phone = _state.value.phoneNumber)

            when (result) {
                is Result.Success -> {
                    _uiEvent.send(VerifyUiEvent.ShowToast(UiText.StringResource(R.string.otp_resent)))
                }
                is Result.Error -> {
                    _state.update { it.copy(error = result.error.toUiText()) }
                }
            }
        }
    }

    // منطق العداد التنازلي
    private fun startResendTimer() {
        viewModelScope.launch {
            while (_state.value.resendTimer > 0) {
                delay(1000L)
                _state.update { it.copy(resendTimer = it.resendTimer - 1) }
            }
            _state.update { it.copy(isResendEnabled = true) }
        }
    }
}