package com.fintech.homework.network.usecase.search

import com.fintech.homework.network.RetrofitClient
import com.fintech.homework.network.services.StreamsService
import com.fintech.homework.presentation.streams.objects.Topic
import io.reactivex.rxjava3.core.Single

interface SearchTopicsUseCase : (Long) -> Single<List<Topic>> {

    override fun invoke(searchQuery: Long): Single<List<Topic>>
}

internal class SearchTopicsUseCaseImpl : SearchTopicsUseCase {

    private val retroClient = RetrofitClient.retrofit.create(StreamsService::class.java)

    override fun invoke(searchQuery: Long): Single<List<Topic>> {

        return retroClient.getTopicsByStreamId(searchQuery)
            .map {
                it.topics.map { topic ->
                    Topic(
                        id = topic.max_id,
                        parent = searchQuery,
                        name = topic.name
                    )
                }
            }

    }

}