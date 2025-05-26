package com.saba.events

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnAddEvent: Button
    private lateinit var listView: ListView
    private val events = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAddEvent = findViewById(R.id.btnAddEvent)
        listView = findViewById(R.id.eventListView)

        btnAddEvent.setOnClickListener {
            startActivity(Intent(this, AddEventActivity::class.java))
        }

        loadEvents()
    }

    override fun onResume() {
        super.onResume()
        loadEvents()
    }

    private fun loadEvents() {
        val sharedPrefs = getSharedPreferences("events", MODE_PRIVATE)
        events.clear()
        events.addAll(sharedPrefs.all.map { "${it.key}: ${it.value}" })
        val adapter = ArrayAdapter(this, R.layout.list_item_event, R.id.tvEventTitle, events)
        listView.adapter = adapter
    }
}
