package com.fintech.homework.data

import androidx.annotation.WorkerThread
import com.fintech.homework.repository.ChannelRepository
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

internal class ChannelRepositoryImpl : ChannelRepository {

    override fun loadChannels(): Observable<List<Channel>> {
        return Observable.fromCallable { generateChannelList() }
            .delay(1000L, TimeUnit.MILLISECONDS)
    }

    @WorkerThread
    private fun generateChannelList(): List<Channel> {
        val channelList = mutableListOf<Channel>()
        for (i in 0..9) {
            channelList.add(Channel(i, "Channel $i"))
        }
        return channelList
    }
}