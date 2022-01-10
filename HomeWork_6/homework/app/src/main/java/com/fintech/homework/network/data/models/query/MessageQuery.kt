package com.fintech.homework.network.data.models.query

data class MessageQuery(
    val type: String,
    val to: List<Int>,
    val topic: String = "",
    val content: String
)