package com.fintech.homework.presentation.streams

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fintech.homework.databinding.StreamItemLayoutBinding
import com.fintech.homework.databinding.TopicItemLayoutBinding
import com.fintech.homework.presentation.streams.listeners.OnStreamClickListener
import com.fintech.homework.presentation.streams.listeners.OnTopicClickedListener
import com.fintech.homework.presentation.streams.objects.StreamTopicItem

sealed class StreamExpandViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    class StreamViewHolder(
        private val viewBinding: StreamItemLayoutBinding
    ) : StreamExpandViewHolder(viewBinding.root) {
        fun bind(stream: StreamTopicItem, listener: OnStreamClickListener) {
            viewBinding.streamTitle.text = stream.name
            viewBinding.root.setOnClickListener {
                listener.onStreamClick(stream.id, it)
            }
        }
    }

    class TopicViewHolder(
        private val viewBinding: TopicItemLayoutBinding
    ) : StreamExpandViewHolder(viewBinding.root) {
        fun bind(topic: StreamTopicItem, topicListener: OnTopicClickedListener) {
            viewBinding.topicNameItem.text = topic.name
            viewBinding.root.setOnClickListener {
                topicListener.onTopicClicked(topic.parentId, topic.name)
            }
        }
    }
}