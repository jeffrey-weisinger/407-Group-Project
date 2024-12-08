package com.cs407.groupproject407

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar

class Tasks private constructor(context: Context) {

    var taskList: MutableList<TaskSummary> = mutableListOf()
    private var taskContext: Context = context

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
        loadTasks()
    }

    fun getTask(taskID: Int): TaskSummary? {
        return taskList.find { it.taskID == taskID }
    }

    fun loadTasks() {
        taskList = mutableListOf() // Clear list

        // Pull tasks from storage
        val sharedPref = taskContext.getSharedPreferences("UserActivities", Context.MODE_PRIVATE)
        val userData = JSONArray(sharedPref.getString("userData", ""))

        for (i in 0..< userData.length()) {
            val currTask = JSONObject(userData.getString(i))

            val currId = currTask.getInt("id")
            val currType = currTask.getString("activityType")
            val currTitle = currTask.getString("activityName")
            val currDate = currTask.getString("date")
            val currTime = currTask.getString("time")

            val currDayOfWeek = calcDayOfWeek(currDate)
            val dayOfWeekFormatted = formatDayOfWeek(taskContext, currDayOfWeek)

            val currRecurring = currTask.getBoolean("recurring")
            val currInfo = currTask.getString("notes")

            taskList.add(TaskSummary(currId, currType, currTitle, currDate, currTime, dayOfWeekFormatted, currRecurring, currInfo))
        }
    }

    // Get day of week as int, given date
    private fun calcDayOfWeek(date: String): Int {
        val splitDate = date.split("-")

        // Get day of week from Calendar
        val day = splitDate[2].toInt()
        val month = splitDate[1].toInt() - 1 // 0 indexed
        val year = splitDate[0].toInt()

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    // Convert int value to day of week string
    private fun formatDayOfWeek(context: Context, dayOfWeek: Int): String {
        // TODO: Choose start of week based on user's setting
        val startOfWeek = 1

        // -1 to map week's start as Monday to Calendar's start as Sunday
        val weekOffset = -1

        val week: Array<String> = context.resources.getStringArray(R.array.starting_day)
        return week[(startOfWeek + dayOfWeek + weekOffset - 1) % 7]
    }
}