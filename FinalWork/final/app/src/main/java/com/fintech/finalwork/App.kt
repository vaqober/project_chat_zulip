package com.fintech.finalwork

import android.app.Application
import android.content.Context
import com.fintech.finalwork.database.AppDatabase


class App : Application() {
    var database: AppDatabase? = null
        private set

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.factory().create(applicationContext)
    }

    companion object {
        var instance: App? = null
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> applicationContext.appComponent
    }