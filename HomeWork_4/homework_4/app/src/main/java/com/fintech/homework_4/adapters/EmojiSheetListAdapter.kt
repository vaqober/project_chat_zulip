package com.fintech.homework_4.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fintech.homework_4.interfaces.OnEmojiClickListener
import com.fintech.homework_4.R

class EmojiSheetListAdapter(
    private val dataSet: List<String>,
    private val listener: OnEmojiClickListener
) : RecyclerView.Adapter<EmojiSheetListAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val listener: OnEmojiClickListener) : RecyclerView.ViewHolder(view) {
        val emojiView: TextView = view as TextView
        fun bind(emoji: String, position: Int) {
            emojiView.text = emoji
            emojiView.setOnClickListener {
                listener.onEmojiClick(position)
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val emojiView = TextView(viewGroup.context)
        emojiView.textSize = viewGroup.resources.getDimension(R.dimen.small_text_size)
        emojiView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        return ViewHolder(emojiView, listener)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.bind(dataSet[position], position)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}