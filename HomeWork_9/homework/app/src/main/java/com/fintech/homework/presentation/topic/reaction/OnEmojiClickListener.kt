package com.fintech.homework.presentation.topic.reaction

import com.fintech.homework.objects.Emoji

interface OnEmojiClickListener {
    fun onEmojiClick(emoji: Emoji)
}