package com.fintech.homework.network.data.models

import kotlinx.serialization.Serializable

@Serializable
data class TopicsResponse (
    val result: String,
    val msg: String,
    val topics: List<TopicInfo>
)

@Serializable
data class TopicInfo(
    val name: String,
    val max_id: Long
)