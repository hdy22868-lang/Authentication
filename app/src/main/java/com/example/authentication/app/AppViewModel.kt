package com.example.authentication.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.authentication.auth.domain.use_cases.IsLoggedInUseCase
import com.example.authentication.auth.presentation.navigation.AuthRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private val isLoggedInUseCase: IsLoggedInUseCase
) : ViewModel() {

    private val _startDestination = MutableStateFlow<AuthRoute?>(null)
    val startDestination: StateFlow<AuthRoute?> = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            _startDestination.value =
                if (isLoggedInUseCase()) AuthRoute.Home else AuthRoute.Welcome
        }
    }
}