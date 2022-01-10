package com.fintech.finalwork.presentation.topic.reaction

import com.fintech.finalwork.objects.Emoji

interface OnEmojiFlexClickListener {
    fun onEmojiFlexClick(messagePosition: Int, emoji: Emoji)
}