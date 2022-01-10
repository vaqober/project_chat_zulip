package com.fintech.homework

import com.fintech.homework.database.AppDatabaseModule
import com.fintech.homework.network.GlideModule
import com.fintech.homework.network.RetrofitModule
import com.fintech.homework.presentation.streams.di.StreamBindModule
import com.fintech.homework.presentation.streams.di.StreamModule
import com.fintech.homework.presentation.topic.di.MessageBindsModule
import com.fintech.homework.presentation.topic.di.MessageModule
import dagger.Binds
import dagger.Module

@Module(
    includes = [
        AppBindModule::class,
        RetrofitModule::class,
        AppDatabaseModule::class,
        StreamBindModule::class,
        StreamModule::class,
        GlideModule::class,
        MessageBindsModule::class,
        MessageModule::class
    ]
)
class AppModule


@Module
interface AppBindModule {
    @Binds
    fun bindApp(app: App): App
}
