package com.example.authentication.app

import com.example.authentication.auth.domain.use_cases.IsLoggedInUseCase
import com.example.authentication.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val appModule = module {
    factoryOf(::IsLoggedInUseCase)
    viewModel { AppViewModel(get()) }
    viewModel { HomeViewModel(get()) }
}