package com.fintech.finalwork.presentation.streams.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class Reducer : DslReducer<Event, State, Effect, Command>() {
    override fun Result.reduce(event: Event): Any {
        return when (event) {
            is Event.Internal.ErrorLoading -> {
                if (state.streams.isEmpty()) {
                    state { copy(isLoading = false, isEmptyState = false) }
                } else {
                    state { copy(streams = state.streams, isLoading = false, isEmptyState = false) }
                    effects { Effect.LoadError(event.error) }
                }
            }
            is Event.Internal.StreamsLoaded -> {
                val itemsList = event.items
                state {
                    copy(
                        streams = itemsList,
                        topics = topics,
                        isLoading = false,
                        isEmptyState = itemsList.isEmpty()
                    )
                }
                commands { +Command.InsertStreamsDB(event.items, event.subscribed) }
            }
            is Event.Internal.StreamsLoadedDB -> {
                val itemsList = event.items
                state {
                    copy(
                        streams = itemsList,
                        topics = topics,
                        isLoading = false,
                        isEmptyState = itemsList.isEmpty()
                    )
                }
            }
            is Event.Internal.TopicLoaded -> {
                val itemsList = state.topics.toMutableMap()
                if (event.items.isNotEmpty()) {
                    itemsList[event.items[0].parentId] = event.items.toSet()
                    state.streams.find { it.id == event.items[0].parentId }!!.isExpanded = true
                    commands { +Command.InsertTopicsDB(event.items) }
                }
                state {
                    copy(
                        streams = streams,
                        topics = itemsList,
                        isLoading = false,
                        isEmptyState = state.streams.isEmpty()
                    )
                }
            }
            is Event.Internal.TopicLoadedDB -> {
                val itemsList = state.topics.toMutableMap()
                if (event.items.isNotEmpty()) {
                    itemsList[event.items[0].parentId] = event.items.toSet()
                    state.streams.find { it.id == event.items[0].parentId }!!.isExpanded = true
                }
                state {
                    copy(
                        streams = streams,
                        topics = itemsList,
                        isLoading = false,
                        isEmptyState = state.streams.isEmpty()
                    )
                }
            }
            is Event.Ui.LoadStreams -> {
                state { copy(isLoading = true, isEmptyState = false) }
                commands { +Command.GetStreamsDB(event.query) }
                commands { +Command.LoadStreams(event.query) }
            }
            is Event.Ui.LoadTopics -> {
                state { copy(isLoading = true, isEmptyState = false) }
                val stream = state.streams.find { it.id == event.stream }
                if (!stream!!.isExpanded) {
                    commands { +Command.GetTopicsByStreamIdDB(event.stream) }
                    commands { +Command.LoadTopicsByStreamId(event.stream) }
                } else {
                    stream.isExpanded = false
                    state {
                        copy(
                            streams = streams,
                            topics = topics,
                            isLoading = false,
                            isEmptyState = state.streams.isEmpty()
                        )
                    }
                }
            }
            is Event.Ui.SubscribeStream -> {
                commands { +Command.SubscribeStream(event.query) }
            }
            is Event.Internal.StreamsInserted -> {
            }
            is Event.Internal.ErrorInsertingDB -> {
                state { copy(streams = state.streams, isLoading = false, isEmptyState = false) }
                effects { Effect.InsertDBError(event.error) }
            }
            is Event.Internal.ErrorSubscribe -> {
                state {
                    copy(
                        streams = state.streams,
                        isLoading = false,
                        isEmptyState = state.streams.isEmpty()
                    )
                }
                effects { Effect.SubscribeError(event.error) }
            }
            is Event.Internal.Subscribed -> {
                commands { +Command.LoadStreams(SearchQuery("", subscribed = true)) }
            }
        }
    }

}