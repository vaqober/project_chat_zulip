package com.fintech.homework_3.interfaces

import com.fintech.homework_3.data.Reaction

interface OnEmojiFlexClickListener {
    fun onEmojiFlexClick(messagePosition: Int, reaction: Reaction)
}