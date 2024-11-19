package com.cs407.groupproject407

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

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

        // Set OnClickListener for addButton and cancelButton (see step 3)
        addButton.setOnClickListener {
            // Retrieve values from input fields
            val activityName = activityNameEditText.text.toString()
            val activityType = activityTypeSpinner.selectedItem.toString()
            val day = activityDatePicker.dayOfMonth
            val month = activityDatePicker.month
            val year = activityDatePicker.year
            val recurring = recurringCheckbox.isChecked
            val notes = notesEditText.text.toString()

            // TODO: Input validation (e.g., check if activityName is empty)

            // Create Intent to pass data back to main activity
            val intent = Intent()
            intent.putExtra("ACTIVITY_NAME", activityName)
            intent.putExtra("ACTIVITY_TYPE", activityType)
            intent.putExtra("DAY", day)
            intent.putExtra("MONTH", month)
            intent.putExtra("YEAR", year)
            intent.putExtra("RECURRING", recurring)
            intent.putExtra("NOTES", notes)

            // Set result and end activity
            setResult(RESULT_OK, intent)
            finish()
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }
}