package com.example.authentication.auth.di

import com.example.authentication.auth.data.repository.FakeAuthRepositoryImpl
import com.example.authentication.auth.domain.repository.AuthRepository
import com.example.authentication.auth.domain.use_cases.LogInOtpUseCase
import com.example.authentication.auth.domain.use_cases.LogInPasswordUseCase
import com.example.authentication.auth.domain.use_cases.LogoutUseCase
import com.example.authentication.auth.domain.use_cases.ResendOtpUseCase
import com.example.authentication.auth.domain.use_cases.ResetPasswordUseCase
import com.example.authentication.auth.domain.use_cases.SignUpUseCase
import com.example.authentication.auth.domain.use_cases.VerifyUseCase
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val fakeAuthModule = module {

    singleOf(::FakeAuthRepositoryImpl) { bind<AuthRepository>() }

    // حقن الـ Use Cases
    factoryOf(::SignUpUseCase)
    factoryOf(::VerifyUseCase)
    factoryOf(::LogInPasswordUseCase)
    factoryOf(::LogInOtpUseCase)
    factoryOf(::ResendOtpUseCase)
    factoryOf(::ResetPasswordUseCase)
    factoryOf(::LogoutUseCase)
}


