package com.fintech.homework.database.objects.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fintech.homework.presentation.topic.reaction.Reaction
import java.util.*

@Entity(
    tableName = "message"
)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    @ColumnInfo(name = "stream_id")
    var streamId: Long,
    @ColumnInfo(name = "topic")
    var topic: String,
    @ColumnInfo(name = "sender_id")
    val senderId: Long,
    @ColumnInfo(name = "sender_name")
    val senderName: String,
    @ColumnInfo(name = "content")
    var content: String,
    @ColumnInfo(name = "timestamp")
    var timestamp: Date,
    @ColumnInfo(name = "reactions")
    val reactions: List<Reaction>
)