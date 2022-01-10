package com.fintech.finalwork.presentation.topic.elm

import com.fintech.finalwork.presentation.topic.message.MessageEntityToItemMapper
import com.fintech.finalwork.presentation.topic.message.MessageInfoToItemMapper
import com.fintech.finalwork.presentation.topic.message.MessageItemToEntityMapper
import com.fintech.finalwork.presentation.topic.message.domain.repository.LoadMessages
import io.reactivex.rxjava3.core.Observable
import vivid.money.elmslie.core.store.Actor

class ActorMessages(
    private val loadMessages: LoadMessages
) : Actor<Command, Event> {

    private val messageEntityToItemMapper: MessageEntityToItemMapper = MessageEntityToItemMapper()
    private val messageItemToEntityMapper: MessageItemToEntityMapper = MessageItemToEntityMapper()
    private val messageInfoToItemMapper: MessageInfoToItemMapper = MessageInfoToItemMapper()

    override fun execute(command: Command): Observable<Event> = when (command) {
        is Command.InsertMessagesDB -> {
            //Save only last 50 elements
            val lastMessages = if (command.messages.size <= 50) {
                command.messages
            } else {
                command.messages.subList(command.messages.size - 50, command.messages.size - 1)
            }
            loadMessages.insertMessagesDB(lastMessages.map(messageItemToEntityMapper))
                .mapEvents(Event.Internal.MessagesInserted) { error ->
                    Event.Internal.ErrorInsertingDB(
                        error
                    )
                }
        }
        is Command.LoadMessages -> {
            loadMessages.loadMessages(command.query)
                .mapEvents(
                    { list ->
                        Event.Internal.MessagesLoaded(list.map(messageInfoToItemMapper))
                    },
                    { error ->
                        Event.Internal.ErrorLoading(error)
                    }
                )
        }
        is Command.PostMessage -> {
            loadMessages.postMessage(command.query)
                .mapEvents(
                    { response -> Event.Internal.MessagePosted(response.id) },
                    { error ->
                        Event.Internal.ErrorPostMessage(error)
                    }
                )
        }
        is Command.PostReaction -> {
            loadMessages.postReaction(command.query)
                .mapEvents(
                    { Event.Internal.ReactionPosted },
                    { error ->
                        Event.Internal.ErrorPostMessage(error)
                    }
                )
        }
        is Command.DeleteReaction -> {
            loadMessages.deleteReaction(command.query)
                .mapEvents(
                    { Event.Internal.ReactionDeleted },
                    { error ->
                        Event.Internal.ErrorPostMessage(error)
                    }
                )
        }
        is Command.SubscribeToDBMessagesLoading -> {
            loadMessages.subscribeToDBMessagesChange(command.stream, command.topic)
                .distinctUntilChanged()
                .toObservable()
                .mapEvents({ list ->
                    Event.Internal.MessagesLoadedFromDB(
                        list.map(messageEntityToItemMapper)
                    )
                },
                    { error ->
                        Event.Internal.ErrorDB(error)
                    }
                )
        }
    }
}