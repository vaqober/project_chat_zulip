package com.fintech.homework_4.data

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