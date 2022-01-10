package com.fintech.homework.adapters

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fintech.homework.customview.CustomMessageViewGroup
import com.fintech.homework.data.Message
import com.fintech.homework.interfaces.BottomSheetDialogListener
import com.fintech.homework.interfaces.OnEmojiFlexClickListener
import java.text.DateFormat
import java.text.SimpleDateFormat


class MessageListAdapter(
    val dataSet: MutableList<Message>,
    private val listenerEmoji: OnEmojiFlexClickListener,
    private val listenerBottomSheet: BottomSheetDialogListener,
) : RecyclerView.Adapter<MessageViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MessageViewHolder {
        // Create a new view, which defines the UI of the list item
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

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: MessageViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.bind(dataSet[position], position)
    }

    @SuppressLint("SimpleDateFormat")
    override fun getItemViewType(position: Int): Int {
        val dateFormatter: DateFormat = SimpleDateFormat("dd MMM")
        return if (position == 0 || !dateFormatter.format(dataSet[position].date)
                .equals(dateFormatter.format(dataSet[position - 1].date))
        ) {
            1
        } else 0
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    fun update(newList: List<Message>) {
        val messageDiffUtilCallback =
            MessageDiffUtilCallback(dataSet, newList)
        DiffUtil.calculateDiff(messageDiffUtilCallback).dispatchUpdatesTo(this)
    }
}