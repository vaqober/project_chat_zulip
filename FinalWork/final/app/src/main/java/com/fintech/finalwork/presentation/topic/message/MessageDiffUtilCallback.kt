package com.fintech.finalwork.presentation.topic.message

import androidx.recyclerview.widget.DiffUtil

class MessageDiffUtilCallback(
    private val mOldList: List<Message>,
    private val mNewList: List<Message>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = mOldList.size

    override fun getNewListSize(): Int = mNewList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        mOldList[oldItemPosition] == mNewList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        mOldList[oldItemPosition].sender_id == mNewList[newItemPosition].sender_id
                && mOldList[oldItemPosition].sender_name == mNewList[newItemPosition].sender_name
                && mOldList[oldItemPosition].content == mNewList[newItemPosition].content
                && mOldList[oldItemPosition].timestamp == mNewList[newItemPosition].timestamp
                && mOldList[oldItemPosition].reactions == mNewList[newItemPosition].reactions

}