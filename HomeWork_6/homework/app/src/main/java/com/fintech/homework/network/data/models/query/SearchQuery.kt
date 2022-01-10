package com.fintech.homework.network.data.models.query

data class SearchQuery(
    val anchor: String,
    val num_before: Int,
    val num_after: Int,
    val stream: Long?,
    val subject: String?,
    val searchQuery: String
)


