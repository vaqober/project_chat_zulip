package com.example.homework_01

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ContactsReceiver(private val activity: SecondActivity) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            CONTACTS_UPDATE -> {
                val data = intent.getStringArrayListExtra("data")
                val dataIntent = Intent()
                dataIntent.putExtra("data", data)
                activity.setResult(Activity.RESULT_OK, dataIntent)
                activity.finish()
            }
            else -> Toast.makeText(context, "Action Not Found", Toast.LENGTH_LONG).show()
        }
    }
}