package com.fintech.homework.presentation.topic.message

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fintech.homework.presentation.topic.customview.CustomMessageViewGroup
import com.fintech.homework.presentation.topic.reaction.BottomSheetDialogListener
import com.fintech.homework.presentation.topic.reaction.OnEmojiFlexClickListener
import java.text.DateFormat
import java.text.SimpleDateFormat


class MessageListAdapter(
    val dataSet: MutableList<Message>,
    private val listenerEmoji: OnEmojiFlexClickListener,
    private val listenerBottomSheet: BottomSheetDialogListener,
) : RecyclerView.Adapter<MessageViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MessageViewHolder {
        val messageView: CustomMessageViewGroup = if (viewType == 1) {
            CustomMessageViewGroup(
                viewGroup.context,
                isHeader = true,
                listenerEmoji = listenerEmoji,
                listenerBottomSheet = listenerBottomSheet
            )
        } else {
            CustomMessageViewGroup(
                viewGroup.context,
                isHeader = false,
                listenerEmoji = listenerEmoji,
                listenerBottomSheet = listenerBottomSheet
            )
        }

        messageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        return MessageViewHolder(messageView)
    }

    override fun onBindViewHolder(viewHolder: MessageViewHolder, position: Int) {
        viewHolder.bind(dataSet[position], position)
    }

    @SuppressLint("SimpleDateFormat")
    override fun getItemViewType(position: Int): Int {
        val dateFormatter: DateFormat = SimpleDateFormat("dd MMM")
        return if (position == 0 || !dateFormatter.format(dataSet[position].timestamp)
                .equals(dateFormatter.format(dataSet[position - 1].timestamp))
        ) {
            1
        } else 0
    }

    override fun getItemCount() = dataSet.size

    fun update(newList: List<Message>) {
        val messageDiffUtilCallback =
            MessageDiffUtilCallback(dataSet, newList)
        DiffUtil.calculateDiff(messageDiffUtilCallback).dispatchUpdatesTo(this)
        this.dataSet.clear()
        this.dataSet.addAll(newList)
    }
}