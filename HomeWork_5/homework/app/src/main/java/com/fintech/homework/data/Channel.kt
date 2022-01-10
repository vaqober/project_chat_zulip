package com.fintech.homework.data

data class Channel(val id: Int, val name: String, val topics: MutableList<Topic> = mutableListOf())