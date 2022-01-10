package com.fintech.homework_4.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fintech.homework_4.R
import com.fintech.homework_4.data.Channel

class ChannelsListAdapter(
    private val dataChannel: Channel,
    private val topicListener: View.OnClickListener
) : RecyclerView.Adapter<ChannelExpandViewHolder>() {

    private var isExpanded: Boolean = false

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ChannelExpandViewHolder {
        // Create a new view, which defines the UI of the list item
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                ChannelExpandViewHolder.ChannelViewHolder(
                    layoutInflater.inflate(R.layout.channel_item_layout, viewGroup, false)
                )
            }
            else -> {
                ChannelExpandViewHolder.TopicViewHolder(
                    layoutInflater.inflate(R.layout.topic_item_layout, viewGroup, false)
                )
            }
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ChannelExpandViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        when (viewHolder) {
            is ChannelExpandViewHolder.ChannelViewHolder -> {
                viewHolder.bind(dataChannel, onChannelClicked())
            }
            is ChannelExpandViewHolder.TopicViewHolder -> {
                viewHolder.bind(dataChannel.topics[position - 1], topicListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_ITEM
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return if (isExpanded) {
            dataChannel.topics.size + 1
        } else {
            1
        }
    }

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_ITEM = 1
    }

    private fun onChannelClicked() = View.OnClickListener {
        isExpanded = !isExpanded
        if (isExpanded) {
            notifyItemRangeInserted(1, dataChannel.topics.size)
            notifyItemChanged(0)
        } else {
            notifyItemRangeRemoved(1, dataChannel.topics.size)
            notifyItemChanged(0)
        }
    }
}