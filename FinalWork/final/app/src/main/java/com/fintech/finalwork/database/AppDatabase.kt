package com.fintech.finalwork.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fintech.finalwork.database.converter.DateConverter
import com.fintech.finalwork.database.converter.ReactionsConverter
import com.fintech.finalwork.database.dao.MessageDao
import com.fintech.finalwork.database.dao.StreamDao
import com.fintech.finalwork.database.dao.TopicDao
import com.fintech.finalwork.database.entity.MessageEntity
import com.fintech.finalwork.database.entity.Stream
import com.fintech.finalwork.database.entity.Topic

@Database(
    entities = [
        Stream::class,
        Topic::class,
        MessageEntity::class
    ], version = 1, exportSchema = false
)
@TypeConverters(ReactionsConverter::class, DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun streamDao(): StreamDao

    abstract fun topicDao(): TopicDao

    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context
        ): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database-zulip"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}