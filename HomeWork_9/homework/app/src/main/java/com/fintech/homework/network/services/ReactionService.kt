package com.fintech.homework.network.services

import com.fintech.homework.network.data.models.ResponseBlankModel
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface ReactionService {

    @FormUrlEncoded
    @POST("messages/{message_id}/reactions")
    fun postReaction(
        @Path("message_id") message_id: Long,
        @Field("emoji_name") emoji_name: String,
        @Field("reaction_type") reaction_type: String
    ): Single<ResponseBlankModel>

    @DELETE("messages/{message_id}/reactions")
    fun deleteReaction(
        @Path("message_id") message_id: Long,
        @Query("emoji_name") emoji_name: String,
        @Query("reaction_type") reaction_type: String
    ): Single<ResponseBlankModel>
}