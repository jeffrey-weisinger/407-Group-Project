package com.cs407.groupproject407

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val fragmentManager = supportFragmentManager
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.calendar).setOnClickListener {
            fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, Calendar::class.java, null)
                .setReorderingAllowed(true)
                .addToBackStack("")
                .commit()
            findViewById<Button>(R.id.upcomingTasks).setBackgroundColor(ContextCompat.getColor(this, R.color.unselected))
            findViewById<Button>(R.id.score).setBackgroundColor(ContextCompat.getColor(this, R.color.unselected))
            findViewById<Button>(R.id.calendar).setBackgroundColor(ContextCompat.getColor(this, R.color.selected))

        }


        findViewById<Button>(R.id.upcomingTasks).setOnClickListener {
            fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, UpcomingTasks::class.java, null)
                .setReorderingAllowed(true)
                .addToBackStack("")
                .commit()
            findViewById<Button>(R.id.upcomingTasks).setBackgroundColor(ContextCompat.getColor(this, R.color.selected))
            findViewById<Button>(R.id.score).setBackgroundColor(ContextCompat.getColor(this, R.color.unselected))
            findViewById<Button>(R.id.calendar).setBackgroundColor(ContextCompat.getColor(this, R.color.unselected))

        }

        findViewById<Button>(R.id.score).setOnClickListener {
            fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, Score::class.java, null)
                .setReorderingAllowed(true)
                .addToBackStack("")
                .commit()
            findViewById<Button>(R.id.upcomingTasks).setBackgroundColor(ContextCompat.getColor(this, R.color.unselected))
            findViewById<Button>(R.id.score).setBackgroundColor(ContextCompat.getColor(this, R.color.selected))
            findViewById<Button>(R.id.calendar).setBackgroundColor(ContextCompat.getColor(this, R.color.unselected))
        }

        findViewById<ImageView>(R.id.settings).setOnClickListener {
            fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, Settings::class.java, null)
                .setReorderingAllowed(true)
                .addToBackStack("")
                .commit()
            findViewById<Button>(R.id.upcomingTasks).setBackgroundColor(ContextCompat.getColor(this, R.color.unselected))
            findViewById<Button>(R.id.score).setBackgroundColor(ContextCompat.getColor(this, R.color.unselected))
            findViewById<Button>(R.id.calendar).setBackgroundColor(ContextCompat.getColor(this, R.color.unselected))

        }

        //set starting id of activities
        val sharedPref = getSharedPreferences("UserActivities", Context.MODE_PRIVATE)
        if (!sharedPref.contains("userData")){
            val editor = sharedPref.edit()
            editor.putString("userData", "[]")
            editor.apply()
        }

        // Apply the theme from SharedPreferences on app start
        val sharedPrefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isLightThemeEnabled = sharedPrefs.getBoolean("light_theme_enabled", true)
        applyTheme(isLightThemeEnabled)


    }

    private fun applyTheme(isLightThemeEnabled: Boolean) {
        if (isLightThemeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}

        //For settings.xml
//        val daySpinner = findViewById<Spinner>(R.id.startingDay)
//        val dayAdapter = ArrayAdapter.createFromResource(
//            this, R.array.starting_day, android.R.layout.simple_spinner_item
//        )
//        daySpinner.adapter = dayAdapter


//For score.xml
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
