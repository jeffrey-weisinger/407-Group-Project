package com.cs407.groupproject407

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

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
    private val taskDate: TextView = itemView.findViewById(R.id.taskDueDate)
    private val taskInfo: TextView = itemView.findViewById(R.id.taskInfo)

    fun bind(taskSummary: TaskSummary) {
        taskTitle.text = taskSummary.taskTitle
        // TODO Use proper date formatting
//        val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
//        taskDate.text = dateFormatter.format(taskSummary.taskDate)
        taskDate.text = taskSummary.taskDate
        taskInfo.text = taskSummary.taskInfo
    }
}
