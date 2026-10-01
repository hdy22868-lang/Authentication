package com.example.authentication.core.di

import com.example.authentication.core.component.phoneNumber.PhoneNumberValidator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule = module {
    single { PhoneNumberValidator(androidContext()) }
}