package com.example.authentication.auth.di

import com.example.authentication.auth.data.local.AuthPreferences
import com.example.authentication.auth.data.remote.dto.AuthRemoteDataSource
import com.example.authentication.auth.data.repository.AuthRepositoryImpl
import com.example.authentication.auth.domain.repository.AuthRepository
import com.example.authentication.auth.domain.use_cases.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authModule = module {

    singleOf(::AuthPreferences)

    singleOf(::AuthRemoteDataSource)

    singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }


    factoryOf(::SignUpUseCase)
    factoryOf(::VerifyUseCase)
    factoryOf(::LogInPasswordUseCase)
    factoryOf(::LogInOtpUseCase)
    factoryOf(::ResendOtpUseCase)
    factoryOf(::ResetPasswordUseCase)
    factoryOf(::LogoutUseCase)
}