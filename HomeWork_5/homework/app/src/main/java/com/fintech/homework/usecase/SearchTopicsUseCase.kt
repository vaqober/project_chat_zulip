package com.fintech.homework.usecase

import com.fintech.homework.data.Topic
import com.fintech.homework.data.TopicRepositoryImpl
import com.fintech.homework.repository.TopicRepository
import io.reactivex.rxjava3.core.Observable

interface SearchTopicsUseCase : (Int) -> Observable<List<Topic>> {

    override fun invoke(searchQuery: Int): Observable<List<Topic>>
}

internal class SearchTopicsUseCaseImpl : SearchTopicsUseCase {

    private val topicRepository: TopicRepository = TopicRepositoryImpl()

    override fun invoke(searchQuery: Int): Observable<List<Topic>> {
        return topicRepository.loadTopics()
            .map { topics ->
                topics.filter {
                    it.parentId == searchQuery
                }
            }
    }

}