package com.fintech.homework.network.data.models.query

import com.fintech.homework.presentation.topic.message.MessageViewModel

data class SearchQuery(
    val anchor: String = MessageViewModel.INITIAL_TYPES,
    val num_before: Int = MessageViewModel.PAGING_COUNT,
    val num_after: Int = 0,
    val stream: Long,
    val subject: String,
    val searchQuery: String = MessageViewModel.INITIAL_QUERY
)


