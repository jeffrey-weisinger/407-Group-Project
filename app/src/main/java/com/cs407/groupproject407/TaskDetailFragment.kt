package com.cs407.groupproject407

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class TaskDetailFragment : Fragment() {

    private var task: TaskSummary? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Get proper taskID
        var taskID = 0
        task = Tasks.getInstance(requireContext()).getTask(taskID)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.task_detail, container, false)

        if (task != null) {
            // TODO: Style task edit screen (add additional data here?)
            val titleTextView = view.findViewById<TextView>(R.id.task_title_detailed)
            titleTextView.text = task!!.taskTitle
            val dateTextView = view.findViewById<TextView>(R.id.task_date_detailed)
            dateTextView.text = task!!.taskDate
            val infoTextView = view.findViewById<TextView>(R.id.task_info_detailed)
            infoTextView.text = task!!.taskInfo
        }

        return view
    }
}