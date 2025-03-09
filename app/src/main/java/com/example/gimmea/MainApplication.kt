package com.example.gimmea

import android.app.Application
import com.example.gimmea.di.appModule
import com.example.gimmea.di.debugViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                appModule,
                debugViewModelModule
            )
        }
    }
}