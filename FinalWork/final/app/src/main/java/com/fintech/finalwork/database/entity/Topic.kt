package com.fintech.finalwork.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "topic",
    primaryKeys = ["parent_id", "name"]
)
data class Topic(
    @ColumnInfo(name = "parent_id")
    val parentId: Long,
    @ColumnInfo(name = "name")
    val name: String
)