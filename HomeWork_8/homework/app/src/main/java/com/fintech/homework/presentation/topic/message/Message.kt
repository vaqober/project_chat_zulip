package com.fintech.homework.presentation.topic.message

import com.fintech.homework.presentation.topic.reaction.Reaction
import java.util.*

data class Message(
    var id: Long = 0,
    var stream_id: Long = 0,
    var topic: String = "",
    val sender_id: Long = 0,
    val sender_name: String = "",
    var content: String = "",
    var timestamp: Date = Date(),
    val reactions: MutableList<Reaction> = mutableListOf()
)