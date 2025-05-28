package com.saba.events

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var btnAddEvent: Button
    private lateinit var listView: ListView
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
        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)

        val eventList = sharedPrefs.all.mapNotNull { entry ->
            val title = entry.key
            val dateString = entry.value as? String ?: return@mapNotNull null
            try {
                val date = sdf.parse(dateString)
                if (date != null) Event(title, date) else null
            } catch (e: Exception) {
                null
            }
        }.sortedByDescending { it.date }

        val adapter = EventAdapter(this, eventList)
        listView.adapter = adapter
    }
}
