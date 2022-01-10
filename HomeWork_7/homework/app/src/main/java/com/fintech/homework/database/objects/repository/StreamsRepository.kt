package com.fintech.homework.database.objects.repository

import com.fintech.homework.database.objects.entity.Stream
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

interface StreamsRepository {
    fun getAll(): Maybe<List<Stream>>
    fun getAllSubscribed(): Maybe<List<Stream>>
    fun findByName(name: String): Maybe<List<Stream>>
    fun insertAll(streams: List<Stream>): Completable
    fun delete(stream: Stream): Completable
}