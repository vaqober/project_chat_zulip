package com.fintech.finalwork.presentation.topic.domain.query

import com.fintech.finalwork.presentation.topic.elm.StoreFactoryMessages

data class SearchQuery(
    val anchor: String = StoreFactoryMessages.INITIAL_TYPES,
    val numBefore: Int = StoreFactoryMessages.PAGING_COUNT,
    val numAfter: Int = 0,
    val stream: Long,
    val subject: String,
    val searchQuery: String = StoreFactoryMessages.INITIAL_QUERY
) {
    fun getNarrow(): String = "[{\"operand\":${this.stream}, \"operator\":\"stream\"}," +
            "{\"operator\": \"topic\", \"operand\": \"${this.subject}\"}]"
}


