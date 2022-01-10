package com.fintech.finalwork.network.services

import com.fintech.finalwork.network.data.models.ResponseSubscribe
import com.fintech.finalwork.network.data.models.StreamsResponse
import com.fintech.finalwork.network.data.models.SubscriptionsResponse
import com.fintech.finalwork.network.data.models.TopicsResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface StreamsService {

    @GET("streams")
    fun loadAllStreams(): Single<StreamsResponse>

    @GET("users/me/subscriptions")
    fun loadSubscribedStreams(): Single<SubscriptionsResponse>

    @GET("users/me/{streamId}/topics")
    fun loadTopicsByStreamId(@Path("streamId") streamId: Long): Single<TopicsResponse>

    @FormUrlEncoded
    @POST("users/me/subscriptions")
    fun subscribeStream(
        @Field("subscriptions") subscriptions: String
    ): Single<ResponseSubscribe>
}