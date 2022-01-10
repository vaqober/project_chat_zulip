package com.fintech.homework.repository

import com.fintech.homework.data.Topic
import io.reactivex.rxjava3.core.Observable

interface TopicRepository {
    fun loadTopics(): Observable<List<Topic>>
}