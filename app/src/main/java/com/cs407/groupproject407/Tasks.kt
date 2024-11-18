package com.cs407.groupproject407

import android.content.Context

class Tasks private constructor(context: Context) {

    var taskList: MutableList<TaskSummary> = mutableListOf()

    companion object {
        private var instance: Tasks? = null

        fun getInstance(context: Context): Tasks {
            if (instance == null) {
                instance = Tasks(context)
            }
            return instance!!
        }
    }

    init {
        // TODO Rather than create new tasks, pull fromm storage
        for (i in 1..10) {
            taskList.add(TaskSummary(i, "Example Task $i"))
        }
    }

    fun getTask(taskID: Int): TaskSummary? {
        return taskList.find { it.taskID == taskID }
    }
}