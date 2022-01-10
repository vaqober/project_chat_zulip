package com.fintech.homework.network.usecase.post

import com.fintech.homework.network.RetrofitClient
import com.fintech.homework.network.data.models.ResponseBlankModel
import com.fintech.homework.network.data.models.ResponseIdModel
import com.fintech.homework.network.data.models.query.MessageQuery
import com.fintech.homework.network.services.MessageService
import io.reactivex.rxjava3.core.Single

interface PostMessageUseCase : (MessageQuery) -> Single<ResponseIdModel> {
    override fun invoke(messageQuery: MessageQuery): Single<ResponseIdModel>
}

internal class PostMessageUseCaseImpl : PostMessageUseCase {

    private val retroClient = RetrofitClient.retrofit.create(MessageService::class.java)

    override fun invoke(messageQuery: MessageQuery): Single<ResponseIdModel> {
        return retroClient.postMessage(
            messageQuery.type,
            messageQuery.to,
            messageQuery.topic,
            messageQuery.content
        )
    }
}