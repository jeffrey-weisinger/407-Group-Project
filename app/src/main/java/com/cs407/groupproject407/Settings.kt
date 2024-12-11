package com.cs407.groupproject407

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment

class Settings : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val settingsView = inflater.inflate(R.layout.settings, container, false)

        val daySpinner = settingsView.findViewById<Spinner>(R.id.startingDay)
        val dayAdapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.starting_day, android.R.layout.simple_spinner_item
        )
        daySpinner.adapter = dayAdapter

        val checkboxSun = settingsView.findViewById<RadioButton>(R.id.checkboxSun)
        val checkboxMoon = settingsView.findViewById<RadioButton>(R.id.checkboxMoon)


        val sharedPrefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)


        // Load theme preference
        val isLightThemeEnabled = sharedPrefs.getBoolean("light_theme_enabled", true)
        applyTheme(isLightThemeEnabled)
        checkboxSun.isChecked = isLightThemeEnabled
        checkboxMoon.isChecked = !isLightThemeEnabled

        checkboxSun.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPrefs.edit()
            if (isChecked) {
                checkboxMoon.isChecked = false // Uncheck the other checkbox
                editor.putBoolean("light_theme_enabled", true)
                applyTheme(true)
            } else {
                checkboxMoon.isChecked = true // Check the other checkbox
                editor.putBoolean("light_theme_enabled", false)
                applyTheme(false)
            }
            editor.apply()
        }

        checkboxMoon.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPrefs.edit()
            if (isChecked) {
                checkboxSun.isChecked = false // Uncheck the other checkbox
                editor.putBoolean("light_theme_enabled", false)
                applyTheme(false)
            } else {
                checkboxSun.isChecked = true // Check the other checkbox
                editor.putBoolean("light_theme_enabled", true)
                applyTheme(true)
            }
            editor.apply()
        }


        val checkbox24Hour = settingsView.findViewById<RadioButton>(R.id.radio24)
        val checkbox12Hour = settingsView.findViewById<RadioButton>(R.id.radio12)

        // Load time format preference
        val is24HourFormat = sharedPrefs.getBoolean("is24HourFormat", true) // Default to 24-hour
        checkbox24Hour.isChecked = is24HourFormat
        checkbox12Hour.isChecked = !is24HourFormat

        checkbox24Hour.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPrefs.edit()
            if (isChecked) {
                checkbox12Hour.isChecked = false
                editor.putBoolean("is24HourFormat", true)
            } else {
                checkbox12Hour.isChecked = true
                editor.putBoolean("is24HourFormat", false)
            }
            editor.apply()
        }

        checkbox12Hour.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPrefs.edit()
            if (isChecked) {
                checkbox24Hour.isChecked = false
                editor.putBoolean("is24HourFormat", false)
            } else {
                checkbox24Hour.isChecked = true
                editor.putBoolean("is24HourFormat", true)
            }
            editor.apply()
        }


        return settingsView
    }

    private fun applyTheme(isLightThemeEnabled: Boolean) {
        if (isLightThemeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }



}