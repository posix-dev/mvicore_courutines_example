package com.shepeliev.livemvi

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

private val modules = listOf(counterModule)

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(modules)
        }
    }
}
