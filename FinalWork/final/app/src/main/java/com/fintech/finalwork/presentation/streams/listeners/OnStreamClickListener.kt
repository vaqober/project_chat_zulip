package com.fintech.finalwork.presentation.streams.listeners

import android.view.View

interface OnStreamClickListener {
    fun onStreamClick(streamId: Long, view: View)
    fun onStreamLongClick(streamId: Long, view: View)
}

