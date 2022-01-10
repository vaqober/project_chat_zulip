package com.fintech.homework.network.usecase.search

import com.fintech.homework.network.RetrofitClient
import com.fintech.homework.network.data.models.StreamInfo
import com.fintech.homework.network.services.StreamsService
import io.reactivex.rxjava3.core.Single

interface SearchStreamsUseCase : (String) -> Single<List<StreamInfo>> {

    override fun invoke(searchQuery: String): Single<List<StreamInfo>>
}

internal class SearchStreamsUseCaseImpl : SearchStreamsUseCase {

    private val retroClient = RetrofitClient.retrofit.create(StreamsService::class.java)

    override fun invoke(searchQuery: String): Single<List<StreamInfo>> {
        return retroClient.getAllStreams().map {
            it.streams.filter { stream ->
                stream.name.contains(
                    searchQuery,
                    ignoreCase = true
                )
            }
        }
    }
}