package com.fintech.homework.database.objects

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fintech.homework.database.objects.converter.DateConverter
import com.fintech.homework.database.objects.converter.ReactionsConverter
import com.fintech.homework.database.objects.dao.MessageDao
import com.fintech.homework.database.objects.dao.StreamDao
import com.fintech.homework.database.objects.dao.TopicDao
import com.fintech.homework.database.objects.dao.UserDao
import com.fintech.homework.database.objects.entity.MessageEntity
import com.fintech.homework.database.objects.entity.Stream
import com.fintech.homework.database.objects.entity.Topic
import com.fintech.homework.database.objects.entity.User

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