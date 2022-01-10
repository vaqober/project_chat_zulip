package com.fintech.homework.di

import com.fintech.homework.App
import com.fintech.homework.domain.LoadStreamsTopics
import com.fintech.homework.network.RetrofitClient
import com.fintech.homework.network.repository.StreamsRepository
import com.fintech.homework.presentation.streams.elm.ActorStreams
import com.fintech.homework.presentation.streams.elm.StoreFactoryStreams

class StreamDI private constructor()  {

    val repository by lazy {StreamsRepository(RetrofitClient.serviceStreams, App.instance!!.database!!.streamDao(), App.instance!!.database!!.topicDao())}

    private val loadStreams by lazy {LoadStreamsTopics(repository)}

    private val actor by lazy { ActorStreams(loadStreams) }

    val subsStreamStoreFactory by lazy { StoreFactoryStreams(actor) }

    val allStreamStoreFactory by lazy { StoreFactoryStreams(actor) }



    companion object {

        lateinit var INSTANCE: StreamDI

        fun init() {
            INSTANCE = StreamDI()
        }
    }
}