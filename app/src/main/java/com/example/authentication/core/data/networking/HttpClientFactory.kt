package com.example.authentication.core.data.networking

import com.example.authentication.auth.domain.model.AuthTokens
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {

    fun create(tokenProvider: TokenProvider): HttpClient {
        return HttpClient {
            // إعدادات تحويل الـ JSON
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true // لتجنب الانهيار إذا أرسل السيرفر حقولاً جديدة لا نعرفها
                    prettyPrint = true
                })
            }

            // إعدادات طباعة الـ Logs لمعرفة ما يحدث في الشبكة
            install(Logging) {
                level = LogLevel.ALL
            }

            // إعدادات المصادقة التلقائية وحقن التوكن
            install(Auth) {
                bearer {
                    loadTokens {
                        // جلب التوكن الحالي المخزن محلياً
                        val accessToken = tokenProvider.getAccessToken()
                        val refreshToken = tokenProvider.getRefreshToken()
                        if (accessToken != null && refreshToken != null) {
                            io.ktor.client.plugins.auth.providers.BearerTokens(accessToken, refreshToken)
                        } else {
                            null
                        }
                    }

                    refreshTokens {
                        // هذه الكتلة تعمل تلقائياً إذا أرجع السيرفر خطأ 401 (Unauthorized)
                        val currentRefreshToken = oldTokens?.refreshToken ?: return@refreshTokens null

                        try {
                            // هنا نقوم بطلب تجديد التوكنات من السيرفر باستخدام الـ Refresh Token
                            val newTokens = tokenProvider.refreshTokens(currentRefreshToken)
                            if (newTokens != null) {
                                io.ktor.client.plugins.auth.providers.BearerTokens(
                                    newTokens.accessToken,
                                    newTokens.refreshToken
                                )
                            } else {
                                null
                            }
                        } catch (e: Exception) {
                            null
                        }
                    }
                }
            }
        }
    }
}

// واجهة مساعدة لتزويد الكلاينت بالتوكنات وإدارتها من التخزين المحلي
interface TokenProvider {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun refreshTokens(refreshToken: String): AuthTokens?
}