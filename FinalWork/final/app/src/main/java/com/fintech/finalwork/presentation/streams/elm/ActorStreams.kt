package com.fintech.finalwork.presentation.streams.elm

import com.fintech.finalwork.presentation.streams.StreamEntityToItemMapper
import com.fintech.finalwork.presentation.streams.StreamToItemMapper
import com.fintech.finalwork.presentation.streams.domain.LoadStreamsTopics
import com.fintech.finalwork.presentation.topic.TopicToItemMapper
import io.reactivex.rxjava3.core.Observable
import vivid.money.elmslie.core.store.Actor

class ActorStreams(
    private val loadStreamsTopics: LoadStreamsTopics
) : Actor<Command, Event> {

    private val streamToItemMapper: StreamToItemMapper = StreamToItemMapper()
    private val streamEntityItemMapper: StreamEntityToItemMapper = StreamEntityToItemMapper()
    private val topicToItemMapper: TopicToItemMapper = TopicToItemMapper()

    override fun execute(command: Command): Observable<Event> = when (command) {
        is Command.LoadStreams -> {
            if (command.query.subscribed) {
                loadStreamsTopics.loadSubscribedStreams(command.query.query)
            } else {
                loadStreamsTopics.loadAllStreams(command.query.query)
            }
                .mapEvents(
                    { list ->
                        Event.Internal.StreamsLoaded(
                            list.map(streamToItemMapper),
                            command.query.subscribed
                        )
                    },
                    { error -> Event.Internal.ErrorLoading(error) }
                )
        }
        is Command.SubscribeStream -> {
            loadStreamsTopics.subscribeStream(command.query)
                .mapEvents(
                    { response ->
                        if (response.result == "success") {
                            val list = response.subscribed
                            if (list.isNullOrEmpty()) {
                                Event.Internal.ErrorSubscribe(Throwable("already subscribed"))
                            } else {
                                Event.Internal.Subscribed(
                                    list[response.subscribed.keys.first().toString()] ?: listOf()
                                )
                            }
                        } else {
                            Event.Internal.ErrorSubscribe(Throwable(response.msg))
                        }
                    },
                    { error -> Event.Internal.ErrorSubscribe(error) }
                )
        }

        is Command.GetStreamsDB -> {
            if (command.query.subscribed) {
                loadStreamsTopics.getSubscribedStreamsDB(command.query.query)
            } else {
                loadStreamsTopics.getAllStreamsDB(command.query.query)
            }
                .mapEvents(
                    { list ->
                        Event.Internal.StreamsLoaded(
                            list.map(streamEntityItemMapper),
                            command.query.subscribed
                        )
                    },
                    { error -> Event.Internal.ErrorLoading(error) }
                )
        }

        is Command.LoadTopicsByStreamId -> {
            loadStreamsTopics.loadTopicsByStreamId(command.stream)
                .mapEvents(
                    { list -> Event.Internal.TopicLoaded(list.map(topicToItemMapper)) },
                    { error -> Event.Internal.ErrorLoading(error) }
                )
        }
        is Command.GetTopicsByStreamIdDB -> {
            loadStreamsTopics.getTopicsByStreamIdDB(command.stream)
                .mapEvents(
                    { list -> Event.Internal.TopicLoadedDB(list.map(topicToItemMapper)) },
                    { error -> Event.Internal.ErrorLoading(error) }
                )
        }
        is Command.InsertStreamsDB -> {
            loadStreamsTopics.insertStreamsDB(command.streams, command.subscribed)
                .mapEvents(
                    Event.Internal.StreamsInserted
                ) { error -> Event.Internal.ErrorInsertingDB(error) }
        }
        is Command.InsertTopicsDB -> {
            loadStreamsTopics.insertTopicsDB(command.topics)
                .mapEvents(
                    Event.Internal.StreamsInserted
                ) { error -> Event.Internal.ErrorInsertingDB(error) }
        }
    }
}
