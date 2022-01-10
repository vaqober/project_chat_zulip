package com.example.homework_01

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button: Button = findViewById(R.id.button)
        val contactsListView: ListView = findViewById(R.id.ListView)
        val contactsArray: ArrayList<String> = arrayListOf()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contactsArray)
        contactsListView.adapter = adapter

        val secondActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val contacts = result.data?.getStringArrayListExtra("data")
                    if (contacts != null && contacts.size > 0) {
                        Toast.makeText(this, "Contacts received", Toast.LENGTH_LONG).show()
                        updateAdapter(
                            adapter,
                            contacts
                        )
                    } else {
                        updateAdapter(adapter, arrayListOf())
                        Toast.makeText(this, "Contacts not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, result.data?.getStringExtra("data"), Toast.LENGTH_SHORT)
                        .show()
                }
            }

        button.setOnClickListener {
            secondActivityResult.launch(Intent(this, SecondActivity::class.java))
        }
    }

    private fun updateAdapter(adapter: ArrayAdapter<String>, array: ArrayList<String>) {
        adapter.clear()
        adapter.addAll(array)
        adapter.notifyDataSetChanged()
    }
}
