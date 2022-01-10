package com.fintech.homework.presentation.streams.domain

import com.fintech.homework.database.entity.Stream
import com.fintech.homework.network.data.models.StreamInfo
import com.fintech.homework.presentation.streams.objects.Topic
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface StreamsRepository {
    fun loadAllStreams(query: String): Single<List<StreamInfo>>

    fun getAllStreamsDB(query: String): Single<List<Stream>>

    fun insertStreams(streams: List<Stream>): Completable

    fun loadSubscribedStreams(query: String): Single<List<StreamInfo>>

    fun getSubscribedStreamsDB(query: String): Single<List<Stream>>

    fun loadTopicsByStreamId(stream: Long): Single<List<Topic>>

    fun getTopicsByStreamIdDB(stream: Long): Single<List<Topic>>

    fun insertTopics(topics: List<com.fintech.homework.database.entity.Topic>): Completable
}