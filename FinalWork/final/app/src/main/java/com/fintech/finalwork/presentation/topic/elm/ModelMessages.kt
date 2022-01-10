package com.fintech.finalwork.presentation.topic.elm

import android.os.Parcelable
import com.fintech.finalwork.presentation.topic.domain.query.MessageQuery
import com.fintech.finalwork.presentation.topic.domain.query.ReactionQuery
import com.fintech.finalwork.presentation.topic.domain.query.SearchQuery
import com.fintech.finalwork.presentation.topic.message.Message
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class State(
    val messages: @RawValue List<Message> = emptyList(),
    val isEmptyState: Boolean = false,
    val currentPage: Int = 0,
    val error: Throwable? = null,
    val isLoading: Boolean = false
) : Parcelable

sealed class Event {

    sealed class Ui : Event() {

        object Init : Ui()

        data class LoadMessages(val query: SearchQuery) : Ui()

        data class SubscribeToDBMessagesLoading(val stream: Long, val topic: String) : Ui()

        data class PostMessage(val query: MessageQuery) : Ui()

        data class PostReaction(val query: ReactionQuery) : Ui()

        data class DeleteReaction(val query: ReactionQuery) : Ui()

    }

    sealed class Internal : Event() {

        data class MessagesLoaded(val items: List<Message>) : Internal()

        data class MessagesLoadedFromDB(val items: List<Message>) : Internal()

        object MessagesInserted : Internal()

        data class MessagePosted(val id: Long) : Internal()

        object ReactionPosted : Internal()

        object ReactionDeleted : Internal()

        data class ErrorLoading(val error: Throwable) : Internal()

        data class ErrorPostMessage(val error: Throwable) : Internal()

        data class ErrorDB(val error: Throwable) : Internal()

        data class ErrorInsertingDB(val error: Throwable) : Internal()

    }
}

sealed class Effect {
    data class LoadError(val error: Throwable) : Effect()

    data class DbError(val error: Throwable) : Effect()

    data class PostError(val error: Throwable) : Effect()

    data class InsertDBError(val error: Throwable) : Effect()
}

sealed class Command {

    data class InsertMessagesDB(val messages: List<Message>) : Command()

    data class SubscribeToDBMessagesLoading(val stream: Long, val topic: String) : Command()

    data class LoadMessages(val query: SearchQuery) : Command()

    data class PostMessage(val query: MessageQuery) : Command()

    data class PostReaction(val query: ReactionQuery) : Command()

    data class DeleteReaction(val query: ReactionQuery) : Command()

}