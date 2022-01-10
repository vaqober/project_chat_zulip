package com.fintech.finalwork.presentation.topic.reaction

import com.fintech.finalwork.objects.Emoji

interface OnEmojiClickListener {
    fun onEmojiClick(emoji: Emoji)
}