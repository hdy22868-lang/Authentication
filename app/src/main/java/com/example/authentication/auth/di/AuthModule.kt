package com.example.authentication.auth.di

import com.example.authentication.auth.data.local.AuthPreferences
import com.example.authentication.auth.data.remote.dto.AuthRemoteDataSource
import com.example.authentication.auth.data.repository.AuthRepositoryImpl
import com.example.authentication.auth.domain.repository.AuthRepository
import com.example.authentication.auth.domain.use_cases.*
import com.example.authentication.auth.presentation.login.logic.LoginViewModel
import com.example.authentication.auth.presentation.reset_password.logic.ResetPasswordViewModel
import com.example.authentication.auth.presentation.signup.logic.SignUpViewModel
import com.example.authentication.auth.presentation.verify_code.logic.VerifyViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
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

    viewModel { LoginViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { VerifyViewModel(get(), get(), get()) }
    viewModel { ResetPasswordViewModel(get(), get()) }
}