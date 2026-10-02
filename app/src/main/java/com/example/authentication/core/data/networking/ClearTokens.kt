package com.example.authentication.core.data.networking

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProviders
import io.ktor.client.plugins.auth.providers.BearerAuthProvider

fun HttpClient.clearBearerTokens() {
    authProviders.filterIsInstance<BearerAuthProvider>().forEach { it.clearToken() }
}