package com.fintech.homework.data

data class Topic(val id: Int, val parentId: Int, val name: String, val messages: MutableList<Message>)