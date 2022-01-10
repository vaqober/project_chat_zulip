package com.example.homework_01

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class SecondActivity : AppCompatActivity() {
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startContactService(this)
            } else {
                val dataIntent = Intent()
                dataIntent.putExtra("data", "PERMISSON DENIED")
                this.setResult(Activity.RESULT_CANCELED, dataIntent)
                this.finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val button: Button = findViewById(R.id.second_button)

        button.setOnClickListener {
            val filterIntent = IntentFilter(CONTACTS_UPDATE)
            val receiver = ContactsReceiver(this)
            LocalBroadcastManager.getInstance(applicationContext)
                .registerReceiver(receiver, filterIntent)
            requestPermission.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    private fun startContactService(context: Context?) {
        Intent(this, ContactsService::class.java).also { intent ->
            startService(intent)
        }
    }
}