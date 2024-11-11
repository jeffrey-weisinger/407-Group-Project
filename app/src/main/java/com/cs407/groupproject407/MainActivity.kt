package com.cs407.groupproject407

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        val workSpinner = findViewById<Spinner>(R.id.workSpinner)
//        val workAdapter = ArrayAdapter.createFromResource(
//            this, R.array.work_array, android.R.layout.simple_spinner_item
//        )
//        workSpinner.adapter = workAdapter
//
//        val recreationSpinner = findViewById<Spinner>(R.id.recreationSpinner)
//        val recreationAdapter = ArrayAdapter.createFromResource(
//            this, R.array.work_array, android.R.layout.simple_spinner_item
//        )
//        recreationSpinner.adapter = recreationAdapter
    }
}