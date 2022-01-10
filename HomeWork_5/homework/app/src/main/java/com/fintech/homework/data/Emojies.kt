package com.fintech.homework.data

object Emojies {
    private val emojiList = listOf(
        "😎",
        "😌",
        "😌",
        "😔",
        "😋",
        "😛",
        "😍",
        "😁",
        "👍",
        "😘",
        "🎃",
        "😊",
        "😚",
        "🙃",
        "☺",
        "😅",
        "😂",
        "😇",
        "😉",
        "😌",
        "😗",
        "😜",
        "😎",
        "😏",
        "😜",
        "😗",
        "😗",
        "😂",
        "😎",
        "😌",
        "😌",
        "😔",
        "😋",
        "😛",
        "😍",
        "😁",
        "👍",
        "😘",
        "🎃",
        "😊",
        "😚",
        "🙃",
        "☺",
        "😅",
        "😂",
        "😇",
        "😉",
        "😌",
        "😗",
        "😜",
        "😎",
        "😏",
        "😜",
        "😗",
        "😗",
        "😂"
    )

    fun getEmojiList(): List<String> {
        return emojiList
    }

    fun getEmoji(position: Int): String {
        return emojiList[position]
    }
}