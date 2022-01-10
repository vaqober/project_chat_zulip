package com.fintech.finalwork.presentation.topic.reaction

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fintech.finalwork.R
import com.fintech.finalwork.objects.Emoji

class EmojiSheetListAdapter(
    private val dataSet: List<Emoji>,
    private val listener: OnEmojiClickListener
) : RecyclerView.Adapter<EmojiSheetListAdapter.ViewHolder>() {

    class ViewHolder(
        view: View, private val listener: OnEmojiClickListener
    ) : RecyclerView.ViewHolder(view) {
        private val emojiView: TextView = view as TextView
        fun bind(emoji: Emoji) {
            emojiView.text = emoji.unicode
            emojiView.setOnClickListener {
                listener.onEmojiClick(emoji)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val emojiView = TextView(viewGroup.context)
        emojiView.textSize = viewGroup.resources.getDimension(R.dimen.small_text_size)
        emojiView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        return ViewHolder(emojiView, listener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size
}