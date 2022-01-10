package com.fintech.homework.presentation.topic.domain.query

import com.fintech.homework.presentation.topic.elm.StoreFactoryMessages

data class SearchQuery(
    val anchor: String = StoreFactoryMessages.INITIAL_TYPES,
    val num_before: Int = StoreFactoryMessages.PAGING_COUNT,
    val num_after: Int = 0,
    val stream: Long,
    val subject: String,
    val searchQuery: String = StoreFactoryMessages.INITIAL_QUERY
) {
    fun getNarrow(): String = "[{\"operand\":${this.stream}, \"operator\":\"stream\"}," +
            "{\"operator\": \"topic\", \"operand\": \"${this.subject}\"}]"
}


