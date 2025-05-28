package com.saba.events

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class EventAdapter(private val context: Context, private val events: List<MainActivity.Event>) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())

    override fun getCount(): Int = events.size

    override fun getItem(position: Int): Any = events[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = convertView ?: inflater.inflate(R.layout.list_item_event, parent, false)

        val tvTitle = rowView.findViewById<TextView>(R.id.tvEventTitle)
        val tvTime = rowView.findViewById<TextView>(R.id.tvEventTime)

        val event = events[position]
        tvTitle.text = event.title
        tvTime.text = dateFormat.format(event.date)

        return rowView
    }
}
