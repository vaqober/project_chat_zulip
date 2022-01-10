package com.fintech.homework.presentation.topic.di

import com.fintech.homework.database.AppDatabase
import com.fintech.homework.network.services.MessageService
import com.fintech.homework.network.services.ReactionService
import com.fintech.homework.presentation.topic.elm.ActorMessages
import com.fintech.homework.presentation.topic.elm.StoreFactoryMessages
import com.fintech.homework.presentation.topic.message.domain.repository.LoadMessages
import com.fintech.homework.presentation.topic.message.domain.repository.MessageRepository
import com.fintech.homework.presentation.topic.message.domain.repository.MessageRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [MessageBindsModule::class])
class MessageModule {

    @Provides
    fun provideMessageService(retrofit: Retrofit): MessageService {
        return retrofit.create(MessageService::class.java)
    }

    @Provides
    fun provideReactionService(retrofit: Retrofit): ReactionService {
        return retrofit.create(ReactionService::class.java)
    }

    @Provides
    fun provideRepository(
        messageService: MessageService,
        reactionService: ReactionService,
        database: AppDatabase
    ): MessageRepositoryImpl {
        return MessageRepositoryImpl(messageService, reactionService, database.messageDao())
    }

    @Provides
    fun provideLoadMessages(repository: MessageRepositoryImpl) = LoadMessages(repository)

    @Provides
    fun provideActor(loadMessages: LoadMessages) = ActorMessages(loadMessages)

    @Provides
    fun provideMessageStoreFactory(actor: ActorMessages) = StoreFactoryMessages(actor)
}

@Module
interface MessageBindsModule {
    @Binds
    fun bindMessageRepository(
        messageRepositoryImpl: MessageRepositoryImpl
    ): MessageRepository
}