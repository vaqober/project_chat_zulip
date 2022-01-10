package com.fintech.homework.usecase

import com.fintech.homework.data.Message
import com.fintech.homework.data.MessagesRepositoryImpl
import com.fintech.homework.repository.MessageRepository
import io.reactivex.rxjava3.core.Observable

interface SearchMessageUseCase : (String) -> Observable<List<Message>> {

    override fun invoke(searchQuery: String): Observable<List<Message>>
}

internal class SearchMessagesUseCaseImpl : SearchMessageUseCase {

    private val messageRepository: MessageRepository = MessagesRepositoryImpl()

    override fun invoke(searchQuery: String): Observable<List<Message>> {
        return messageRepository.loadMessages()
            .map { messages ->
                messages.filter {
                    it.message.contains(searchQuery, ignoreCase = true)
                }
            }
    }

}