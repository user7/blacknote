package com.gb.blacknote.di

import android.app.Application
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(koinModule)
        }
    }
}