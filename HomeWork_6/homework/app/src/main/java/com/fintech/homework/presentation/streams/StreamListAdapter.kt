package com.fintech.homework.presentation.streams

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fintech.homework.databinding.StreamItemLayoutBinding
import com.fintech.homework.databinding.TopicItemLayoutBinding
import com.fintech.homework.presentation.streams.listeners.OnStreamClickListener
import com.fintech.homework.presentation.streams.listeners.OnTopicClickedListener
import com.fintech.homework.presentation.streams.objects.StreamTopicItem
import com.fintech.homework.presentation.streams.objects.Types.VIEW_TYPE_CHANNEL
import com.fintech.homework.presentation.streams.objects.Types.VIEW_TYPE_TOPIC

internal class StreamListAdapter(
    private val dataSet: MutableList<StreamTopicItem>,
    private val topicListener: OnTopicClickedListener,
    private val streamListener: OnStreamClickListener
) : RecyclerView.Adapter<StreamExpandViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamExpandViewHolder {
        when (viewType) {
            VIEW_TYPE_CHANNEL -> {
                return StreamExpandViewHolder.StreamViewHolder(
                    StreamItemLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            VIEW_TYPE_TOPIC -> {
                return StreamExpandViewHolder.TopicViewHolder(
                    TopicItemLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
        return StreamExpandViewHolder.StreamViewHolder(
            StreamItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StreamExpandViewHolder, position: Int) {
        when (holder) {
            is StreamExpandViewHolder.StreamViewHolder -> {
                holder.bind(dataSet[position], streamListener)
            }
            is StreamExpandViewHolder.TopicViewHolder -> {
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

    fun update(newList: List<StreamTopicItem>) {
        val channelDiffUtilCallback = StreamItemDiffUtilCallback(dataSet, newList)
        DiffUtil.calculateDiff(channelDiffUtilCallback)
            .dispatchUpdatesTo(this)
        dataSet.clear()
        dataSet.addAll(newList)
    }

    fun expandTopics(newList: List<StreamTopicItem>) {
        val channelIndex = dataSet.indexOf(dataSet.find { it.id == newList[0].parentId })
        val oldDataSet = dataSet.toList()
        dataSet.addAll(channelIndex + 1, newList)
        dataSet[channelIndex].isExpanded = true
        update(dataSet.toList())
        val channelDiffUtilCallback = StreamItemDiffUtilCallback(oldDataSet, dataSet)
        DiffUtil.calculateDiff(channelDiffUtilCallback)
            .dispatchUpdatesTo(this)
    }

    class StreamItemDiffUtilCallback(
        private val mOldList: List<StreamTopicItem>,
        private val mNewList: List<StreamTopicItem>
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