package com.fintech.homework_4.data

object Topics {
    fun getStubTopic(id: Int): Topic {
        val list = mutableListOf<Message>()
        for (i in 0..10) {
            list.add(Message(i, "Darrel", "Hello lorem ipsum pipsum"))
        }
        return Topic(id, "Topic", list)
    }
}