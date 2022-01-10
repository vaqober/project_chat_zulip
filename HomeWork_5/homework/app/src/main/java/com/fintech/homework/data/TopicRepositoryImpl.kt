package com.fintech.homework.data

import androidx.annotation.WorkerThread
import com.fintech.homework.repository.TopicRepository
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

class TopicRepositoryImpl : TopicRepository {

    override fun loadTopics(): Observable<List<Topic>> {
        return Observable.fromCallable { generateTopicList() }
            .delay(1000L, TimeUnit.MILLISECONDS)
    }

    @WorkerThread
    private fun generateTopicList(): List<Topic> {
        val listMessage = mutableListOf<Message>()
        val topicList = mutableListOf<Topic>()
        for (i in 0..10) {
            listMessage.add(Message(i, "Darrel", "Hello lorem ipsum pipsum"))
        }
        for (i in 0..10) {
            for (j in 0..3) {
                topicList.add(Topic(j, i,"Topic", listMessage))
            }
        }

        return topicList
    }
}