package com.fintech.homework.interfaces

import com.fintech.homework.data.Reaction

interface OnEmojiFlexClickListener {
    fun onEmojiFlexClick(messagePosition: Int, reaction: Reaction)
}