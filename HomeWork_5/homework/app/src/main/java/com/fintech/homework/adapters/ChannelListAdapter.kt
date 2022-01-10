package com.fintech.homework.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fintech.homework.adapters.Types.VIEW_TYPE_CHANNEL
import com.fintech.homework.adapters.Types.VIEW_TYPE_TOPIC
import com.fintech.homework.data.ChannelTopicItem
import com.fintech.homework.databinding.ChannelItemLayoutBinding
import com.fintech.homework.databinding.TopicItemLayoutBinding
import com.fintech.homework.interfaces.OnChannelClickListener

internal class ChannelListAdapter(
    private val dataSet: MutableList<ChannelTopicItem>,
    private val topicListener: View.OnClickListener,
    private val channelListener: OnChannelClickListener
) : RecyclerView.Adapter<ChannelExpandViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelExpandViewHolder {
        when (viewType) {
            VIEW_TYPE_CHANNEL -> {
                return ChannelExpandViewHolder.ChannelViewHolder(
                    ChannelItemLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_TYPE_TOPIC -> {
                return ChannelExpandViewHolder.TopicViewHolder(
                    TopicItemLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
        return ChannelExpandViewHolder.ChannelViewHolder(
            ChannelItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChannelExpandViewHolder, position: Int) {
        when (holder) {
            is ChannelExpandViewHolder.ChannelViewHolder -> {
                holder.bind(dataSet[position], channelListener)
            }
            is ChannelExpandViewHolder.TopicViewHolder -> {
                holder.bind(dataSet[position], topicListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return dataSet[position].type
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun update(newList: List<ChannelTopicItem>) {
        val channelDiffUtilCallback = ChannelItemDiffUtilCallback(dataSet, newList)
        DiffUtil.calculateDiff(channelDiffUtilCallback)
            .dispatchUpdatesTo(this)
        dataSet.clear()
        dataSet.addAll(newList)
    }

    fun expandTopics(newList: List<ChannelTopicItem>) {
        val channelIndex = dataSet.indexOf(dataSet.find { it.id == newList[0].parentId && it.type == VIEW_TYPE_CHANNEL })
        val oldDataSet = dataSet.toList()
        dataSet.addAll(channelIndex + 1, newList)
        dataSet[channelIndex].isExpanded = true
        val channelDiffUtilCallback = ChannelItemDiffUtilCallback(oldDataSet, dataSet)
        DiffUtil.calculateDiff(channelDiffUtilCallback)
            .dispatchUpdatesTo(this)
    }

    class ChannelItemDiffUtilCallback(
        private val mOldList: List<ChannelTopicItem>,
        private val mNewList: List<ChannelTopicItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = mOldList.size

        override fun getNewListSize(): Int = mNewList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            mOldList[oldItemPosition] == mNewList[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            mOldList[oldItemPosition].id == mNewList[newItemPosition].id
                    && mOldList[oldItemPosition].parentId == mNewList[newItemPosition].parentId
                    && mOldList[oldItemPosition].type == mNewList[newItemPosition].type
                    && mOldList[oldItemPosition].name == mNewList[newItemPosition].name
                    && mOldList[oldItemPosition].isExpanded == mNewList[newItemPosition].isExpanded

    }
}