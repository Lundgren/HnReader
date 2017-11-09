package io.lundgren.hnreader.presentation

import android.app.Application
import io.lundgren.hnreader.presentation.di.AppComponent
import io.lundgren.hnreader.presentation.di.DaggerAppComponent

class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
                .builder()
                .build()
    }
}