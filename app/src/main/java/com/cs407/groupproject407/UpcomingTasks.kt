package com.cs407.groupproject407

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class UpcomingTasks : Fragment() {
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.upcoming_tasks, container, false)
        taskRecyclerView = view.findViewById(R.id.taskRecyclerView)

        val onClickListener = View.OnClickListener { itemView: View ->
            val selectedTaskId = itemView.tag as Int
            Log.d("TEST", "This task was clicked! ID: $selectedTaskId")
            // TODO: Open task editor (using task_detail.xml)
        }

        val tasks = Tasks.getInstance(requireContext()).taskList
        adapter = TaskAdapter(tasks, onClickListener)
        taskRecyclerView.adapter = adapter

        return view
    }
}