package com.cs407.groupproject407

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
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

        val checkboxSun = settingsView.findViewById<CheckBox>(R.id.checkboxSun)
        val blankPanel = settingsView.findViewById<CheckBox>(R.id.BlankPanel)
        val sharedPrefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        // Load theme preference
        val isLightThemeEnabled = sharedPrefs.getBoolean("light_theme_enabled", true)
        applyTheme(isLightThemeEnabled)

        checkboxSun.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPrefs.edit()
            if (isChecked) {
                blankPanel.isChecked = false // Uncheck the other checkbox
                editor.putBoolean("light_theme_enabled", true)
                applyTheme(true)
            } else {
                blankPanel.isChecked = true // Check the other checkbox
                editor.putBoolean("light_theme_enabled", false)
                applyTheme(false)
            }
            editor.apply()
        }

        blankPanel.setOnCheckedChangeListener { _, isChecked ->
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