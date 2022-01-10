package com.fintech.homework.presentation.topic.elm

import com.fintech.homework.objects.CurrentUser
import com.fintech.homework.presentation.topic.message.Message
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class ReducerMessages : DslReducer<Event, State, Effect, Command>() {
    override fun Result.reduce(event: Event): Any {
        return when (event) {
            is Event.Ui.LoadMessages -> {
                state { copy(isLoading = true, error = null, isEmptyState = false) }
                commands { +Command.LoadMessages(event.query) }
            }
            is Event.Ui.PostMessage -> {
                val message = event.query
                val messages = state.messages.toMutableList()
                messages.add(
                    Message(
                        -1,
                        message.to[0],
                        message.topic,
                        CurrentUser.id,
                        CurrentUser.name,
                        message.content,
                    )
                )
                state { copy(messages = messages, currentPage = 0, error = null, isEmptyState = false) }
                commands { +Command.PostMessage(event.query) }
            }
            is Event.Ui.PostReaction -> {
                commands { +Command.PostReaction(event.query) }
            }
            is Event.Ui.DeleteReaction -> {
                commands { +Command.DeleteReaction(event.query) }
            }
            is Event.Ui.SubscribeToDBMessagesLoading -> {
                commands { +Command.SubscribeToDBMessagesLoading(event.stream, event.topic) }
            }
            is Event.Internal.MessagesLoaded -> {
                state {
                    copy(
                        messages = event.items,
                        currentPage = event.items.size / StoreFactoryMessages.PAGING_COUNT,
                        isLoading = false,
                        error = null,
                        isEmptyState = event.items.isEmpty()
                    )
                }
                commands { +Command.InsertMessagesDB(event.items) }
            }
            is Event.Internal.MessagesLoadedFromDB -> {
                val messages = state.messages.toMutableList()
                messages.dropLast(event.items.size)
                messages.addAll(event.items)
                state {
                    copy(
                        messages = messages,
                        isLoading = false,
                        error = null,
                        isEmptyState = event.items.isEmpty()
                    )
                }
            }
            Event.Internal.MessagesInserted -> {
                state {
                    copy(
                        isLoading = false,
                        error = null
                    )
                }
            }
            is Event.Internal.MessagePosted -> {
                val messages = state.messages.toMutableList()
                messages[messages.size - 1].id = event.id
                state {
                    copy(
                        messages = messages,
                        isLoading = false,
                        error = null,
                        isEmptyState = messages.isEmpty()
                    )
                }
            }
            Event.Internal.ReactionDeleted -> {
            }
            Event.Internal.ReactionPosted -> {
            }
            is Event.Internal.ErrorDB -> {
                if (state.messages.isEmpty()) {
                    state { copy(error = event.error, isLoading = false, isEmptyState = true) }
                } else {
                    state {
                        copy(
                            messages = state.messages,
                            isLoading = false,
                            isEmptyState = false
                        )
                    }
                    effects { Effect.DbError(event.error) }
                }
            }
            is Event.Internal.ErrorPostMessage -> {
                if (state.messages.isEmpty()) {
                    state { copy(error = event.error, isLoading = false, isEmptyState = true) }
                } else {
                    val messages = state.messages.toMutableList()
                    messages.removeAt(messages.size - 1)
                    state {
                        copy(
                            messages = messages,
                            isLoading = false,
                            isEmptyState = false
                        )
                    }
                    effects { Effect.PostError(event.error) }
                }
            }
            is Event.Internal.ErrorInsertingDB -> {
                effects { Effect.InsertDBError(event.error) }
            }
            is Event.Internal.ErrorLoading -> {
                if (state.messages.isEmpty()) {
                    state { copy(error = event.error, isLoading = false, isEmptyState = true) }
                } else {
                    state {
                        copy(
                            messages = state.messages,
                            isLoading = false,
                            isEmptyState = false
                        )
                    }
                    effects { Effect.LoadError(event.error) }
                }
            }
            Event.Ui.Init -> {
                state { copy(isLoading = true, error = null, isEmptyState = true) }
            }
        }
    }

}