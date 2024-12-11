package com.cs407.groupproject407

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalTime
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.cs407.groupproject407.databinding.ActivityMainBinding
import java.util.Calendar
import java.util.Date

class AddActivity : AppCompatActivity() {

    private lateinit var activityNameEditText: EditText
    private lateinit var activityTypeSpinner: Spinner
    private lateinit var activityDatePicker: DatePicker
    private lateinit var recurringCheckbox: CheckBox
    private lateinit var notesEditText: EditText
    private lateinit var addButton: Button
    private lateinit var cancelButton: Button
    private lateinit var timePicker: TimePicker

    private lateinit var binding: ActivityMainBinding
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(this, "Please allow all notifications to continue.", Toast.LENGTH_LONG).show()
        }
    }

    @VisibleForTesting
    public fun requestPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            Toast.makeText(this, "Android Version out of date!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (ContextCompat.checkSelfPermission(
                applicationContext, android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            Toast.makeText(this, "Please enable all notifications to continue.", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    private fun requestScheduleExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {  // API 31 and higher
            // If the permission is not granted, we need to request it
            if (!isExactAlarmPermissionGranted()) {
                // Intent to request the user to enable SCHEDULE_EXACT_ALARM in the settings
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivityForResult(intent, 0)  // Launch the request settings intent
            }
        }
    }

    // Checks if the app has the necessary permission to schedule exact alarms
    private fun isExactAlarmPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // For API 31 and higher (Android 12 and above), check if exact alarm permission is granted
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.canScheduleExactAlarms()
        } else {
            // For lower API levels, permission is automatically granted
            true
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add)

        // Get references to the views
        activityNameEditText = findViewById(R.id.activity_name_edittext)
        activityTypeSpinner = findViewById(R.id.activity_type_spinner)
        activityDatePicker = findViewById(R.id.activity_date_picker)
        recurringCheckbox = findViewById(R.id.recurring_checkbox)
        notesEditText = findViewById(R.id.notes_edittext)
        addButton = findViewById(R.id.add_button)
        cancelButton = findViewById(R.id.cancel_button)

        createNotificationChannel()
        requestPermission()


        // Populate the activity type spinner
        val activityTypes = arrayOf("Work", "School", "Recreation", "Meeting", "Social")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, activityTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        activityTypeSpinner.adapter = adapter

        val extras = intent.extras
        if (extras?.containsKey("year") == true) {
            val sentYear = extras.getInt("year")
            val sentMonth = extras.getInt("month") - 1
            val sentDay = extras.getInt("day")
            activityDatePicker.updateDate(sentYear, sentMonth, sentDay)

        }


        // Set OnClickListener for addButton and cancelButton (see step 3)
        addButton.setOnClickListener {
            // Retrieve values from input fields
            val activityName = activityNameEditText.text.toString()
            if (activityName == ""){
                Toast.makeText(this, "Please choose an Activity Name!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val activityType = activityTypeSpinner.selectedItem.toString()
            var day = (activityDatePicker.dayOfMonth).toString()
            if (day.length < 2){
                day = "0$day"
            }
            val month = (activityDatePicker.month + 1).toString()
            val year = (activityDatePicker.year).toString()
            val recurring = recurringCheckbox.isChecked
            val notes = notesEditText.text.toString()
            val date = "$year-$month-$day"



            timePicker = findViewById(R.id.activity_time_picker)

            //add timePicker's listener if necessary
//            timePicker.setOnTimeChangedListener { _, hour, minute ->
//
//            }



            val currHour = timePicker.hour
            val currMin = timePicker.minute

            val sharedPref = getSharedPreferences("UserActivities", Context.MODE_PRIVATE)
            val id = sharedPref.getInt("activityId", 1)
            val jsonObj = JSONObject()
            jsonObj.put("id", id)
            jsonObj.put("activityName", activityName)
            jsonObj.put("activityType", activityType)
            jsonObj.put("date", date)
            jsonObj.put("time", "$currHour:$currMin")
            jsonObj.put("recurring", recurring)
            jsonObj.put("notes", notes)

            val editor = sharedPref.edit()

            var jsonArr = JSONArray(sharedPref.getString("userData", ""))
            jsonArr.put(jsonObj)
            editor.putString("userData", jsonArr.toString())
            editor.putInt("activityId", id+1)

            editor.apply()

            //notifications:
            if (requestPermission() != true){
                return@setOnClickListener
            }

            if (!isExactAlarmPermissionGranted()){
                Toast.makeText(this, "Please enable alarm notifications to continue.", Toast.LENGTH_SHORT).show()
                requestScheduleExactAlarmPermission()
                return@setOnClickListener
            }

            // Check if notification permissions are granted
            if (checkNotificationPermissions(this)) {
                // Schedule a notification
                scheduleNotification()
            }

            finish()
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }
    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotification() {


        // Create an intent for the Notification BroadcastReceiver
        val intent = Intent(applicationContext, NotificationReceiver::class.java)
        val activityType = activityTypeSpinner.selectedItem.toString()
        val activityName = activityNameEditText.text.toString()
        // Extract title and message from user input
        val title = activityType + " Time!"
        val message = activityName


        // Add title and message as extras to the intent
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)
        val sharedPref = getSharedPreferences("UserActivities", Context.MODE_PRIVATE)
        val reqCode = sharedPref.getInt("notificationId", 1)
        intent.putExtra("requestCode", reqCode)
        sharedPref.edit().putInt("notificationId", reqCode + 1).apply()
        // Create a PendingIntent for the broadcast
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            reqCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Get the AlarmManager service
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Get the selected time and schedule the notification
        val time = getTime()

        if (time <= System.currentTimeMillis()) {
            // Trigger the notification immediately
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(intent.getStringExtra(titleExtra))
                .setContentText(intent.getStringExtra(messageExtra))
                .build()

            notificationManager.notify(notificationID, notification)
        } else {
            // If the time is in the future, schedule it normally
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
        }
    }

    private fun showAlert(time: Long, title: String, message: String) {
        // Format the time for display
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        // Create and show an alert dialog with notification details
        AlertDialog.Builder(this)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: $title\nMessage: $message\nAt: ${dateFormat.format(date)} ${timeFormat.format(date)}"
            )
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    private fun getTime(): Long {

        // Get selected time from TimePicker and DatePicker
        val minute = timePicker.minute
        val hour = timePicker.hour
        val day = activityDatePicker.dayOfMonth
        val month = activityDatePicker.month
        val year = activityDatePicker.year

        // Create a Calendar instance and set the selected date and time
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)


        return calendar.timeInMillis
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        // Create a notification channel for devices running
        // Android Oreo (API level 26) and above
        val name = "Notify Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc

        // Get the NotificationManager service and create the channel
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun checkNotificationPermissions(context: Context): Boolean {
        // Check if notification permissions are granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val isEnabled = notificationManager.areNotificationsEnabled()

            if (!isEnabled) {
                // Open the app notification settings if notifications are not enabled
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                context.startActivity(intent)

                return false
            }
        } else {
            val areEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()

            if (!areEnabled) {
                // Open the app notification settings if notifications are not enabled
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                context.startActivity(intent)

                return false
            }
        }

        // Permissions are granted
        return true
    }

}