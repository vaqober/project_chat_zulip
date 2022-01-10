package com.fintech.finalwork.network.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicsResponse(
    val result: String,
    val msg: String,
    val topics: List<TopicInfo>
)

@Serializable
data class TopicInfo(
    val name: String,
    @SerialName("max_id")
    val maxId: Long
)