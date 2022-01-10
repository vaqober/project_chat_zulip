package com.fintech.finalwork.presentation.topic.domain.query

data class MessageQuery(
    val type: String,
    val to: List<Long>,
    val topic: String = "",
    val content: String
)