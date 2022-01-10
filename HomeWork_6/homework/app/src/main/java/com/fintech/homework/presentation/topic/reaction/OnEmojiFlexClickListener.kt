package com.fintech.homework.presentation.topic.reaction

import com.fintech.homework.objects.Emoji

interface OnEmojiFlexClickListener {
    fun onEmojiFlexClick(messagePosition: Int, emoji: Emoji)
}