package com.example.authentication.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.auth.domain.use_cases.LogoutUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _loggedOut = Channel<Unit>(Channel.BUFFERED)
    val loggedOut = _loggedOut.receiveAsFlow()

    fun onLogoutClick() {
        viewModelScope.launch {
            logoutUseCase()
            _loggedOut.send(Unit)
        }
    }
}