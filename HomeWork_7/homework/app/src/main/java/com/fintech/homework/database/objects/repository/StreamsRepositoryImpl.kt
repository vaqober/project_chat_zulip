package com.fintech.homework.database.objects.repository

import com.fintech.homework.database.objects.dao.StreamDao
import com.fintech.homework.database.objects.entity.Stream
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

class StreamsRepositoryImpl(private val streamDao: StreamDao) : StreamsRepository {

    override fun getAll(): Maybe<List<Stream>> {
        return streamDao.getAll()
    }

    override fun getAllSubscribed(): Maybe<List<Stream>> {
        return streamDao.loadAllSubscribed()
    }

    override fun findByName(name: String): Maybe<List<Stream>> {
        return streamDao.findByName(name)
    }

    override fun insertAll(streams: List<Stream>): Completable {
        return streamDao.insertAll(streams)
    }

    override fun delete(stream: Stream): Completable {
        return streamDao.delete(stream)
    }

}