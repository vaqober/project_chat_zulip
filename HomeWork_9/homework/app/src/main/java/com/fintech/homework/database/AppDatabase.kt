package com.fintech.homework.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fintech.homework.database.converter.DateConverter
import com.fintech.homework.database.converter.ReactionsConverter
import com.fintech.homework.database.dao.MessageDao
import com.fintech.homework.database.dao.StreamDao
import com.fintech.homework.database.dao.TopicDao
import com.fintech.homework.database.dao.UserDao
import com.fintech.homework.database.entity.MessageEntity
import com.fintech.homework.database.entity.Stream
import com.fintech.homework.database.entity.Topic
import com.fintech.homework.database.entity.User

@Database(
    entities = [
        User::class,
        Stream::class,
        Topic::class,
        MessageEntity::class
    ], version = 1, exportSchema = false
)
@TypeConverters(ReactionsConverter::class, DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

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