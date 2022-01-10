package com.fintech.homework

import android.app.Application
import com.fintech.homework.database.objects.AppDatabase


class App : Application() {
    var database: AppDatabase? = null
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = AppDatabase.getDatabase(this)
    }

    companion object {
        var instance: App? = null
    }
}