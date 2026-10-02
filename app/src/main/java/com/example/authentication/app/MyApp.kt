package com.example.authentication.app

import android.app.Application
import com.example.authentication.BuildConfig
import com.example.authentication.auth.di.fakeAuthModule
import com.example.authentication.auth.di.authModule
import com.example.authentication.core.di.coreModule
import com.example.authentication.core.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MyApp)

            modules(coreModule, networkModule, appModule, if (BuildConfig.USE_FAKE) fakeAuthModule else authModule)
        }
    }
}