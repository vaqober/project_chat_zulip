package com.fintech.finalwork.presentation.streams.domain

import com.fintech.finalwork.database.entity.Stream
import com.fintech.finalwork.network.data.models.ResponseSubscribe
import com.fintech.finalwork.network.data.models.StreamInfo
import com.fintech.finalwork.presentation.streams.elm.Subscribe
import com.fintech.finalwork.presentation.streams.objects.StreamTopicItem
import com.fintech.finalwork.presentation.streams.objects.Topic
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class LoadStreamsTopics @Inject constructor(
    private val streamsRepository: StreamsRepositoryImpl
) {

    fun loadAllStreams(query: String): Single<List<StreamInfo>> {
        return streamsRepository.loadAllStreams(query)
    }

    fun getAllStreamsDB(query: String): Single<List<Stream>> {
        return streamsRepository.getAllStreamsDB(query)
    }

    fun insertStreamsDB(streams: List<StreamTopicItem>, subscribed: Boolean): Completable {
        return streamsRepository.insertStreams(
            streams.map { Stream(it.id, it.name, subscribed) })
    }

    fun loadSubscribedStreams(query: String): Single<List<StreamInfo>> {
        return streamsRepository.loadSubscribedStreams(query)
    }

    fun getSubscribedStreamsDB(query: String): Single<List<Stream>> {
        return streamsRepository.getSubscribedStreamsDB(query)
    }

    fun loadTopicsByStreamId(stream: Long): Single<List<Topic>> {
        return streamsRepository.loadTopicsByStreamId(stream)
    }

    fun getTopicsByStreamIdDB(stream: Long): Single<List<Topic>> {
        return streamsRepository.getTopicsByStreamIdDB(stream)
    }

    fun insertTopicsDB(topics: List<StreamTopicItem>): Completable {
        return streamsRepository.insertTopics(topics.map {
            com.fintech.finalwork.database.entity.Topic(
                parentId = it.parentId,
                name = it.name
            )
        })
    }

    fun subscribeStream(subscribe: Subscribe): Single<ResponseSubscribe> {
        return streamsRepository.subscribeStream(subscribe)
    }
}