package com.fintech.homework_4.interfaces

import com.fintech.homework_4.data.Reaction

interface OnEmojiFlexClickListener {
    fun onEmojiFlexClick(messagePosition: Int, reaction: Reaction)
}