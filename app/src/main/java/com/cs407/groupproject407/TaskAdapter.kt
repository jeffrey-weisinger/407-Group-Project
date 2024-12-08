package com.cs407.groupproject407

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import android.content.Context

class TaskAdapter(
    private val taskList: List<TaskSummary>,
    private val onClick: View.OnClickListener,
) : RecyclerView.Adapter<TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.task_list_item, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val taskSummary = taskList[position]
        holder.bind(taskSummary)
        holder.itemView.tag = taskSummary.taskID
        holder.itemView.setOnClickListener(onClick)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}

class TaskViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    private val taskTitle: TextView = itemView.findViewById(R.id.taskTitle)
    private val taskType: TextView = itemView.findViewById(R.id.taskType)
    private val taskDate: TextView = itemView.findViewById(R.id.taskDueDate)
    private val taskInfo: TextView = itemView.findViewById(R.id.taskInfo)

    fun bind(taskSummary: TaskSummary) {
        taskTitle.text = taskSummary.taskTitle
        taskType.text = taskSummary.taskType

        val formattedTime = formatTime(taskSummary.taskTime)
        val dayOfWeek = taskSummary.dayOfWeek

        val splitDate = taskSummary.taskDate.split("-")
        var formattedDate = "Due: ${splitDate[1]}/${splitDate[2]}/${splitDate[0]} at $formattedTime"

        if (taskSummary.isRecurring) {
            formattedDate += "\nRecurring ${dayOfWeek}s"
        }

        taskDate.text = formattedDate
        taskInfo.text = taskSummary.taskInfo
    }

    // Formats time
    private fun formatTime(time: String): String {
        val splitTime = time.split(":").toMutableList()

        // TODO: Use 12/24 hour based on user's setting
        val format = 12

        var timeOfDay = "AM"
        if (format == 12) {
            if (splitTime[0].toInt() > 12) {
                splitTime[0] = (splitTime[0].toInt() - 12).toString()
                timeOfDay = "PM"
            }
            if (splitTime[1].length == 1) {
                splitTime[1] += "0"
            }
        } else if (format == 24) {
            timeOfDay = ""
        }

        return "${splitTime[0]}:${splitTime[1]} $timeOfDay"
    }
}
