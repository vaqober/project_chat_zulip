package com.fintech.finalwork

import com.fintech.finalwork.database.AppDatabaseModule
import com.fintech.finalwork.network.GlideModule
import com.fintech.finalwork.network.RetrofitModule
import com.fintech.finalwork.presentation.streams.di.StreamBindModule
import com.fintech.finalwork.presentation.streams.di.StreamModule
import com.fintech.finalwork.presentation.topic.di.MessageBindsModule
import com.fintech.finalwork.presentation.topic.di.MessageModule
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
