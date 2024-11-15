package com.cs407.groupproject407

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
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
        return settingsView
    }



}