package com.cs407.groupproject407

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment

class Score : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val scoreView = inflater.inflate(R.layout.score, container, false)

        val workSpinner = scoreView.findViewById<Spinner>(R.id.workSpinner)
        val workAdapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.work_array, android.R.layout.simple_spinner_item
        )
        workSpinner.adapter = workAdapter

        val recreationSpinner = scoreView.findViewById<Spinner>(R.id.recreationSpinner)
        val recreationAdapter = ArrayAdapter.createFromResource(
            requireContext(), R.array.work_array, android.R.layout.simple_spinner_item
        )
        recreationSpinner.adapter = recreationAdapter
        return scoreView
    }
}