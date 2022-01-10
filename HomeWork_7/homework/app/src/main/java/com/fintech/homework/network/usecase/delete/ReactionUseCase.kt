package com.fintech.homework.network.usecase.delete

import com.fintech.homework.network.RetrofitClient
import com.fintech.homework.network.data.models.ResponseBlankModel
import com.fintech.homework.network.data.models.query.Action
import com.fintech.homework.network.data.models.query.ReactionQuery
import com.fintech.homework.network.services.ReactionService
import com.fintech.homework.network.usecase.ReactionUseCase
import io.reactivex.rxjava3.core.Single

internal class ReactionUseCaseImpl : ReactionUseCase {

    private val retroClient = RetrofitClient.retrofit.create(ReactionService::class.java)

    override fun invoke(reactionQuery: ReactionQuery): Single<ResponseBlankModel> {
        when (reactionQuery.action) {
            Action.DELETE -> return retroClient.deleteReaction(
                reactionQuery.message_id,
                reactionQuery.emoji_name,
                reactionQuery.reaction_type,
            )
            Action.POST -> return retroClient.postReaction(
                reactionQuery.message_id,
                reactionQuery.emoji_name,
                reactionQuery.reaction_type
            )
            else -> return retroClient.postReaction(
                reactionQuery.message_id,
                reactionQuery.emoji_name,
                reactionQuery.reaction_type
            )
        }

    }
}