package com.fintech.homework.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fintech.homework.data.ChannelTopicItem
import com.fintech.homework.databinding.ChannelItemLayoutBinding
import com.fintech.homework.databinding.TopicItemLayoutBinding
import com.fintech.homework.interfaces.OnChannelClickListener

sealed class ChannelExpandViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    class ChannelViewHolder(
        private val viewBinding: ChannelItemLayoutBinding
    ) : ChannelExpandViewHolder(viewBinding.root) {
        fun bind(channel: ChannelTopicItem, listener: OnChannelClickListener) {
            viewBinding.channelName.text = channel.name
            viewBinding.root.setOnClickListener {
                listener.onChannelClick(channel.id, it)
            }
        }
    }

    class TopicViewHolder(
        private val viewBinding: TopicItemLayoutBinding
    ) : ChannelExpandViewHolder(viewBinding.root) {
        fun bind(topic: ChannelTopicItem, topicListener: View.OnClickListener) {
            viewBinding.topicName.text = topic.name
            viewBinding.root.setOnClickListener {
                topicListener.onClick(it)
            }
        }
    }
}