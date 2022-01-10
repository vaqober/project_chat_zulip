package com.fintech.homework.network.usecase.search

import com.fintech.homework.network.RetrofitClient
import com.fintech.homework.network.data.models.MessageInfo
import com.fintech.homework.network.data.models.query.SearchQuery
import com.fintech.homework.network.services.MessageService
import io.reactivex.rxjava3.core.Single

interface SearchMessageUseCase : (SearchQuery) -> Single<List<MessageInfo>> {
    override fun invoke(searchQuery: SearchQuery): Single<List<MessageInfo>>
}

internal class SearchMessagesUseCaseImpl : SearchMessageUseCase {

    private val retroClient = RetrofitClient.retrofit.create(MessageService::class.java)

    override fun invoke(searchQuery: SearchQuery): Single<List<MessageInfo>> {
        return retroClient.getMessages(
            searchQuery.anchor,
            searchQuery.num_before,
            searchQuery.num_after,
            "[{\"operand\":${searchQuery.stream}, \"operator\":\"stream\"}," +
                    "{\"operator\": \"topic\", \"operand\": \"${searchQuery.subject}\"}]"
        ).map {
            it.messages.filter { message ->
                message.content.contains(searchQuery.searchQuery, ignoreCase = true)
            }
        }
    }
}

