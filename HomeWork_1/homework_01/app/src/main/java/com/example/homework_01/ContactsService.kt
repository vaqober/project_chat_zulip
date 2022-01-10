package com.example.homework_01

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.IBinder
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class ContactsService() : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val data = getContacts()
        sendIntent(data)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

    private fun sendIntent(data: ArrayList<String>) {
        Intent().also { intent ->
            intent.action = CONTACTS_UPDATE
            intent.putExtra("data", data)
            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
        }
    }

    private fun getContacts(): ArrayList<String> {
        val contentResolver = contentResolver
        val cursor: Cursor? =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        val contacts = ArrayList<String>()

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val contact =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))
                contacts.add(contact)
            }
            cursor.close()
        }
        return contacts
    }
}