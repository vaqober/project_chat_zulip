package com.fintech.finalwork.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppDatabaseModule {

    private val databaseName = "database-zulip"

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        databaseName
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideStreamDao(db: AppDatabase) = db.streamDao()

    @Provides
    @Singleton
    fun provideTopicDao(db: AppDatabase) = db.topicDao()

    @Provides
    @Singleton
    fun provideMessageDao(db: AppDatabase) = db.messageDao()
}