package com.fintech.homework_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fintech.homework_2.customview.EmojiTextView
import com.fintech.homework_2.customview.FlexibleLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val flex = findViewById<FlexibleLayout>(R.id.flex)
        for (i in 1..9) {
            flex.addView(EmojiTextView(this, smile = "\uD83D\uDE03", count = i))
        }

        flex.addView(EmojiTextView(this, smile = "➕"))
    }
}