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

class AddActivity : AppCompatActivity() {

    private lateinit var activityNameEditText: EditText
    private lateinit var activityTypeSpinner: Spinner
    private lateinit var activityDatePicker: DatePicker
    private lateinit var recurringCheckbox: CheckBox
    private lateinit var notesEditText: EditText
    private lateinit var addButton: Button
    private lateinit var cancelButton: Button

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


        // Populate the activity type spinner
        val activityTypes = arrayOf("Work", "School", "Recreation", "Meeting")
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
            val day = (activityDatePicker.dayOfMonth).toString()
            val month = (activityDatePicker.month + 1).toString()
            val year = (activityDatePicker.year).toString()
            val recurring = recurringCheckbox.isChecked
            val notes = notesEditText.text.toString()
            val date = "$year-$month-$day"


            // TODO: Input validation (e.g., check if activityName is empty)

            val timePicker: TimePicker = findViewById(R.id.activity_time_picker)
            val currentTime = LocalTime.now()
            var currHour = currentTime.hour
            var currMinute = currentTime.minute
            timePicker.hour = currHour
            timePicker.minute = currMinute

            timePicker.setOnTimeChangedListener { _, hour, minute ->
                // Perform actions when the time is changed
                currHour = hour
                currMinute = minute
                println("Time changed to: $hour:$minute")
            }




            val sharedPref = getSharedPreferences("UserActivities", Context.MODE_PRIVATE)
            val id = sharedPref.getInt("activityId", 0)
            val jsonObj = JSONObject()
            jsonObj.put("id", id)
            jsonObj.put("activityName", activityName)
            jsonObj.put("activityType", activityType)
            jsonObj.put("date", date)
            jsonObj.put("time", "$currHour:$currMinute")
            jsonObj.put("recurring", recurring)
            jsonObj.put("notes", notes)

            val editor = sharedPref.edit()

            var jsonArr = JSONArray(sharedPref.getString("userData", ""))
            Log.i("CURRENT_DATA1", jsonArr.toString())
            jsonArr.put(jsonObj)
            editor.putString("userData", jsonArr.toString())
            editor.putInt("activityId", id+1)

            editor.apply()

            finish()
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }
}