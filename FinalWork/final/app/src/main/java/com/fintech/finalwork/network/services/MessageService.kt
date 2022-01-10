package com.fintech.finalwork.network.services

import com.fintech.finalwork.network.data.models.MessagesResponse
import com.fintech.finalwork.network.data.models.ResponseIdModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface MessageService {
    @GET("messages")
    fun getMessages(
        @Query("anchor") anchor: String,
        @Query("num_before") numBefore: Int,
        @Query("num_after") numAfter: Int,
        @Query("narrow") narrow: String
    ): Single<MessagesResponse>

    @FormUrlEncoded
    @POST("messages")
    fun postMessage(
        @Field("type") type: String,
        @Field("to") to: List<Long>,
        @Field("topic") topic: String,
        @Field("content") content: String
    ): Single<ResponseIdModel>
}