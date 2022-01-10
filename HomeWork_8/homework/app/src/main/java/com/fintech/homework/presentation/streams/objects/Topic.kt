package com.fintech.homework.presentation.streams.objects

data class Topic(val id: Long, val parent: Long, val name: String) {
    override fun toString(): String {
        return "$parent,$name"
    }
}