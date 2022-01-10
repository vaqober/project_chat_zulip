package com.fintech.finalwork.presentation.streams.di

import com.fintech.finalwork.database.AppDatabase
import com.fintech.finalwork.network.services.StreamsService
import com.fintech.finalwork.presentation.streams.domain.LoadStreamsTopics
import com.fintech.finalwork.presentation.streams.domain.StreamsRepository
import com.fintech.finalwork.presentation.streams.domain.StreamsRepositoryImpl
import com.fintech.finalwork.presentation.streams.elm.ActorStreams
import com.fintech.finalwork.presentation.streams.elm.StoreFactoryStreams
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [StreamBindModule::class])
class StreamModule {

    @Provides
    fun provideStreamsService(retrofit: Retrofit): StreamsService {
        return retrofit.create(StreamsService::class.java)
    }

    @Provides
    fun provideRepository(
        streamsService: StreamsService,
        database: AppDatabase
    ): StreamsRepositoryImpl {
        return StreamsRepositoryImpl(streamsService, database.streamDao(), database.topicDao())
    }

    @Provides
    fun provideLoadStreams(repository: StreamsRepositoryImpl) = LoadStreamsTopics(repository)

    @Provides
    fun provideActor(loadStreamsTopics: LoadStreamsTopics) = ActorStreams(loadStreamsTopics)

    @Provides
    fun provideStreamStoreFactory(actor: ActorStreams) = StoreFactoryStreams(actor)
}

@Module
interface StreamBindModule {
    @Binds
    fun bindStreamRepository(
        streamsRepositoryImpl: StreamsRepositoryImpl
    ): StreamsRepository
}