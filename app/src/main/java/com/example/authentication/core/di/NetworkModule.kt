package com.example.authentication.core.di

import com.example.authentication.BuildConfig
import com.example.authentication.core.data.networking.AuthTokenProvider
import com.example.authentication.core.data.networking.HttpClientFactory
import com.example.authentication.core.data.networking.SessionManager
import com.example.authentication.core.data.networking.TokenProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single { SessionManager() }
    single(named("plain"))
    { HttpClient(OkHttp)
    { install(ContentNegotiation)
    { json(Json { ignoreUnknownKeys = true;
        isLenient = true }) }; defaultRequest { url(BuildConfig.BASE_URL) } } }
    single<TokenProvider> { AuthTokenProvider(get(), get(named("plain")), get()) }
    single { HttpClientFactory.create(get()) }
}