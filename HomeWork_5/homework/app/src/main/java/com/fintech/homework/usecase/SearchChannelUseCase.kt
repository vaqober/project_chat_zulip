package com.fintech.homework.usecase

import com.fintech.homework.data.Channel
import com.fintech.homework.data.ChannelRepositoryImpl
import com.fintech.homework.repository.ChannelRepository
import io.reactivex.rxjava3.core.Observable

interface SearchChannelsUseCase : (String) -> Observable<List<Channel>> {

    override fun invoke(searchQuery: String): Observable<List<Channel>>
}

internal class SearchChannelsUseCaseImpl : SearchChannelsUseCase {

    private val channelRepository: ChannelRepository = ChannelRepositoryImpl()

    override fun invoke(searchQuery: String): Observable<List<Channel>> {
        return channelRepository.loadChannels()
            .map { channels ->
                channels.filter {
                    it.name.contains(searchQuery, ignoreCase = true)
                }
            }
    }

}