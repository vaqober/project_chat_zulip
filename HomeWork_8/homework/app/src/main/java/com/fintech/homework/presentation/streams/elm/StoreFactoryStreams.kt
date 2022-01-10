package com.fintech.homework.presentation.streams.elm

import vivid.money.elmslie.core.store.ElmStore

class StoreFactoryStreams(
    private val actor: ActorStreams
) {

    private val store by lazy {
        ElmStore(
            initialState = State(),
            reducer = Reducer(),
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