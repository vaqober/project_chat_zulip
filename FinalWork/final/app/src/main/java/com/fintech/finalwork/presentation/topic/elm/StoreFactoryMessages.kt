package com.fintech.finalwork.presentation.topic.elm

import vivid.money.elmslie.core.store.ElmStore

class StoreFactoryMessages(private val actor: ActorMessages) {

    private val store by lazy {
        ElmStore(
            initialState = State(),
            reducer = ReducerMessages(),
            actor = actor
        )
    }

    fun provide() = store

    companion object {
        const val INITIAL_QUERY: String = ""
        const val PAGING_COUNT: Int = 25
        const val INITIAL_TYPES: String = "newest"
    }
}