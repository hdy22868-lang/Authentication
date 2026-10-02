package com.example.authentication.core.data.networking

import com.example.authentication.auth.data.local.AuthPreferences
import com.example.authentication.auth.data.remote.dto.request.RefreshRequestDto
import com.example.authentication.auth.data.remote.dto.response.AuthResponseDto
import com.example.authentication.auth.domain.model.AuthTokens
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.first
import kotlin.coroutines.cancellation.CancellationException
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthTokenProvider(
    private val prefs: AuthPreferences,
    private val plainClient: HttpClient,
    private val session: SessionManager
) : TokenProvider {

    override suspend fun getAccessToken() = prefs.accessTokenFlow.first()
    override suspend fun getRefreshToken() = prefs.refreshTokenFlow.first()

    override suspend fun refreshTokens(refreshToken: String): AuthTokens? {
        return try {
            val response = plainClient.post(ApiConfig.REFRESH) {
                contentType(ContentType.Application.Json)
                setBody(RefreshRequestDto(refreshToken))
            }
            if (response.status.value in listOf(401, 403)) { expire(); return null }
            if (!response.status.isSuccess()) return null
            val dto = response.body<AuthResponseDto>()
            val access = dto.accessToken
            val refresh = dto.refreshToken
            if (access.isNullOrBlank() || refresh.isNullOrBlank()) { expire(); return null }
            prefs.saveTokens(access, refresh)
            AuthTokens(access, refresh)
        } catch (e: CancellationException) { throw e
        } catch (e: Exception) { null } // انقطاع شبكة: لا نخرج المستخدم
    }

    private suspend fun expire() {
        prefs.clearTokens()
        session.notifyExpired()
    }
}