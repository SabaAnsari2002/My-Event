package com.saba.events

import android.app.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class AddEventActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var btnDate: Button
    private lateinit var btnTime: Button
    private lateinit var btnSave: Button
    private var selectedCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        etTitle = findViewById(R.id.etEventTitle)
        btnDate = findViewById(R.id.btnSelectDate)
        btnTime = findViewById(R.id.btnSelectTime)
        btnSave = findViewById(R.id.btnSave)

        // درخواست مجوز نوتیفیکیشن (اندروید 13 به بالا)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 100)
        }

        btnDate.setOnClickListener {
            val now = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                selectedCalendar.set(Calendar.YEAR, y)
                selectedCalendar.set(Calendar.MONTH, m)
                selectedCalendar.set(Calendar.DAY_OF_MONTH, d)
            }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).show()
        }

        btnTime.setOnClickListener {
            val now = Calendar.getInstance()
            TimePickerDialog(this, { _, h, min ->
                selectedCalendar.set(Calendar.HOUR_OF_DAY, h)
                selectedCalendar.set(Calendar.MINUTE, min)
                selectedCalendar.set(Calendar.SECOND, 0)
            }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true).show()
        }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            if (title.isNotBlank()) {
                val sharedPrefs = getSharedPreferences("events", MODE_PRIVATE).edit()
                sharedPrefs.putString(title, selectedCalendar.time.toString())
                sharedPrefs.apply()

                scheduleNotification(title, selectedCalendar.timeInMillis)

                Toast.makeText(this, "مناسبت ثبت شد!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun scheduleNotification(title: String, time: Long) {
        val intent = Intent(this, NotificationReceiver::class.java)
        intent.putExtra("eventTitle", title)
        val pending = PendingIntent.getBroadcast(
            this,
            title.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                val permissionIntent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = Uri.parse("package:$packageName")
                }
                startActivity(permissionIntent)
                Toast.makeText(this, "لطفاً اجازه‌ی زمان‌بندی دقیق را در تنظیمات فعال کنید", Toast.LENGTH_LONG).show()
                return
            }
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pending)
    }
}
