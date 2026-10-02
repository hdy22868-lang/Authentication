package com.example.authentication.core.data.networking

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class SessionManager {
    private val _expired = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val expired = _expired.asSharedFlow()
    fun notifyExpired() { _expired.tryEmit(Unit) }
}