package com.fintech.finalwork.presentation.streams.elm

import android.os.Parcelable
import com.fintech.finalwork.presentation.streams.objects.StreamTopicItem
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.Serializable

@Parcelize
data class State(
    val streams: @RawValue List<StreamTopicItem> = emptyList(),
    val topics: @RawValue Map<Long, Set<StreamTopicItem>> = emptyMap(),
    val isEmptyState: Boolean = false,
    val isLoading: Boolean = false
) : Parcelable

sealed class Event {

    sealed class Ui : Event() {

        data class LoadStreams(val query: SearchQuery) : Ui()

        data class LoadTopics(val stream: Long) : Ui()

        data class SubscribeStream(val query: Subscribe) : Ui()

    }

    sealed class Internal : Event() {

        data class StreamsLoaded(val items: List<StreamTopicItem>, val subscribed: Boolean) :
            Internal()

        data class StreamsLoadedDB(val items: List<StreamTopicItem>, val subscribed: Boolean) :
            Internal()

        data class Subscribed(val streams: List<String>) : Internal()

        object StreamsInserted : Internal()

        data class TopicLoaded(val items: List<StreamTopicItem>) : Internal()

        data class TopicLoadedDB(val items: List<StreamTopicItem>) : Internal()

        data class ErrorLoading(val error: Throwable) : Internal()

        data class ErrorSubscribe(val error: Throwable) : Internal()

        data class ErrorInsertingDB(val error: Throwable) : Internal()

    }
}

sealed class Effect {
    data class LoadError(val error: Throwable) : Effect()

    data class InsertDBError(val error: Throwable) : Effect()

    data class SubscribeError(val error: Throwable) : Effect()
}

sealed class Command {

    data class GetStreamsDB(val query: SearchQuery) : Command()

    data class InsertStreamsDB(val streams: List<StreamTopicItem>, val subscribed: Boolean) : Command()

    data class LoadStreams(val query: SearchQuery) : Command()

    data class LoadTopicsByStreamId(val stream: Long) : Command()

    data class SubscribeStream(val query: Subscribe) : Command()

    data class GetTopicsByStreamIdDB(val stream: Long) : Command()

    data class InsertTopicsDB(val topics: List<StreamTopicItem>) : Command()

}

data class SearchQuery(val query: String, val subscribed: Boolean)

@Serializable
data class Subscribe(val description: String, val name: String)