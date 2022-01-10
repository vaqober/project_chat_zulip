package com.fintech.homework.adapters

import androidx.recyclerview.widget.DiffUtil
import com.fintech.homework.data.Message

class MessageDiffUtilCallback(
    private val mOldList: List<Message>,
    private val mNewList: List<Message>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = mOldList.size

    override fun getNewListSize(): Int = mNewList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        mOldList[oldItemPosition] == mNewList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        mOldList[oldItemPosition].userId == mNewList[newItemPosition].userId
                && mOldList[oldItemPosition].userName == mNewList[newItemPosition].userName
                && mOldList[oldItemPosition].message == mNewList[newItemPosition].message
                && mOldList[oldItemPosition].date == mNewList[newItemPosition].date
                && mOldList[oldItemPosition].reactions == mNewList[newItemPosition].reactions

}