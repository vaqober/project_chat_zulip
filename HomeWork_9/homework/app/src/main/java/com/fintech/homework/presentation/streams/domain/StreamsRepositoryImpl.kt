package com.fintech.homework.presentation.streams.domain

import com.fintech.homework.database.dao.StreamDao
import com.fintech.homework.database.dao.TopicDao
import com.fintech.homework.database.entity.Stream
import com.fintech.homework.network.data.models.StreamInfo
import com.fintech.homework.network.services.StreamsService
import com.fintech.homework.presentation.streams.objects.Topic
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class StreamsRepositoryImpl @Inject constructor(
    private val service: StreamsService,
    private val streamDao: StreamDao,
    private val topicDao: TopicDao
) : StreamsRepository {

    override fun loadAllStreams(query: String): Single<List<StreamInfo>> {
        return service.loadAllStreams()
            .map { it.streams.filter { stream -> stream.name.contains(query) } }
    }

    override fun getAllStreamsDB(query: String): Single<List<Stream>> {
        return streamDao.getAll().map { it.filter { stream -> stream.name.contains(query) } }
    }

    override fun loadSubscribedStreams(query: String): Single<List<StreamInfo>> {
        return service.loadSubscribedStreams()
            .map { it.subscriptions.filter { stream -> stream.name.contains(query) } }
    }

    override fun getSubscribedStreamsDB(query: String): Single<List<Stream>> {
        return streamDao.getAllSubscribed()
            .map { it.filter { stream -> stream.name.contains(query) } }
    }

    override fun loadTopicsByStreamId(stream: Long): Single<List<Topic>> {
        return service.loadTopicsByStreamId(stream)
            .map {
                it.topics.map { topic ->
                    Topic(
                        id = -1,
                        parent = stream,
                        name = topic.name
                    )
                }
            }
    }

    override fun getTopicsByStreamIdDB(stream: Long): Single<List<Topic>> {
        return topicDao.getAllByStreamId(stream)
            .map {
                it.map { topic ->
                    Topic(
                        id = topic.id,
                        parent = topic.parentId,
                        name = topic.name
                    )
                }
            }
    }

    override fun insertTopics(topics: List<com.fintech.homework.database.entity.Topic>): Completable {
        return topicDao.insertAll(topics)
    }

    override fun insertStreams(streams: List<Stream>): Completable {
        return streamDao.insertAll(streams)
    }
}