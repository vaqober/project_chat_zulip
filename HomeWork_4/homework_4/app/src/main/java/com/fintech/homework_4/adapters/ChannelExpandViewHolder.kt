package com.fintech.homework_4.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fintech.homework_4.R
import com.fintech.homework_4.data.Channel
import com.fintech.homework_4.data.Topic

sealed class ChannelExpandViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    class ChannelViewHolder(view: View) : ChannelExpandViewHolder(view) {
        private val viewChannel = view
        fun bind(channel: Channel, listener: View.OnClickListener) {
            viewChannel.findViewById<TextView>(R.id.channel_name).text = channel.name
            viewChannel.setOnClickListener {
                listener.onClick(it)
            }
        }
    }

    class TopicViewHolder(view: View) : ChannelExpandViewHolder(view) {
        private val viewTopic = view
        fun bind(topic: Topic, topicListener: View.OnClickListener) {
            viewTopic.findViewById<TextView>(R.id.topic_name).text = topic.name
            viewTopic.setOnClickListener {
                topicListener.onClick(it)
            }
        }
    }
}