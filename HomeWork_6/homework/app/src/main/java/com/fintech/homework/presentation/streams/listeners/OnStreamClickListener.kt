package com.fintech.homework.presentation.streams.listeners

import android.view.View

interface OnStreamClickListener {
    fun onStreamClick(streamId: Long, view: View)
}