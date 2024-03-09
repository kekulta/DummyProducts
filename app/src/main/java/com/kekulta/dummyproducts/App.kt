package com.kekulta.dummyproducts

import android.app.Application
import com.kekulta.dummyproducts.di.koinModule
import com.kekulta.dummyproducts.di.viewModelsModule
import logcat.AndroidLogcatLogger
import logcat.LogPriority
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

        setLogger()
        startKoin()
    }

    private fun setLogger() {
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)
    }

    private fun startKoin() {
        startKoin {
            androidContext(instance)
            modules(
                viewModelsModule,
                koinModule,
            )
        }
    }

    companion object {
        lateinit var instance: App
            private set
    }
}