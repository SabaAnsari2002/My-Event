package com.saba.events

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var btnAddEvent: Button
    private lateinit var listView: ListView
    private val events = mutableListOf<String>()
    data class Event(val title: String, val date: Date)

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
        val eventList = sharedPrefs.all.mapNotNull { entry ->
            val title = entry.key
            val dateString = entry.value as? String ?: return@mapNotNull null
            try {
                val date = Date(dateString)
                Event(title, date)
            } catch (e: Exception) {
                null
            }
        }.sortedByDescending { it.date }

        val adapter = EventAdapter(this, eventList)
        listView.adapter = adapter
    }
}
