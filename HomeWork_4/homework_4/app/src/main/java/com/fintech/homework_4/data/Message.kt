package com.fintech.homework_4.data

import java.util.*

data class Message(
    val userId: Int = -1,
    var userName: String = "",
    var message: String = "",
    val reactions: MutableList<Reaction> = mutableListOf(),
    var date: Date = Date(0)
)