package com.fintech.homework.database.objects.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "topic"
)
data class Topic(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "parent_id")
    val parentId: Long,
    @ColumnInfo(name = "name")
    val name: String
)