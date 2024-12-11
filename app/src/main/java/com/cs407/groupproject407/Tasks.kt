package com.cs407.groupproject407

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar

class Tasks private constructor(context: Context) {

    var taskList: MutableList<TaskSummary> = mutableListOf()
    private val week: Array<String> = context.resources.getStringArray(R.array.starting_day)

    companion object {
        private var instance: Tasks? = null

        fun getInstance(context: Context): Tasks {
            if (instance == null) {
                instance = Tasks(context)
            }
            return instance!!
        }
    }

    fun getTask(taskID: Int): TaskSummary? {
        return taskList.find { it.taskID == taskID }
    }

    // Load tasks from given sharedPreferences data
    fun loadTasks(sharedPref: SharedPreferences) {
        taskList = mutableListOf() // Clear list

        // Pull tasks from storage
        val userData = JSONArray(sharedPref.getString("userData", ""))

        for (i in 0..< userData.length()) {
            val currTask = JSONObject(userData.getString(i))

            val currId = currTask.getInt("id")
            val currType = currTask.getString("activityType")
            val currTitle = currTask.getString("activityName")
            val currDate = currTask.getString("date")
            val currTime = currTask.getString("time")

            val currDayOfWeek = calcDayOfWeek(currDate)
            val dayOfWeekFormatted = formatDayOfWeek(currDayOfWeek)

            val currRecurring = currTask.getBoolean("recurring")
            val currInfo = currTask.getString("notes")

            taskList.add(TaskSummary(currId, currType, currTitle, currDate, currTime, dayOfWeekFormatted, currRecurring, currInfo))
        }

        // Sort list by date and time
        taskList.sortBy { task ->
            // Format date for sorting as an int
            var taskDateToList = task.taskDate.split("-")
            taskDateToList = taskDateToList.map{date ->
                if (date.length < 2) {
                    return@map "0${date}"
                } else {
                    return@map date
                }
            }

            // Format time for sorting as an int
            var taskTimeToList = task.taskTime.split(":")
            taskTimeToList = taskTimeToList.map{time ->
                if (time.length < 2) {
                    return@map "0${time}"
                } else {
                    return@map time
                }
            }

            val taskDateFinal = taskDateToList.reduce{ prev, cur -> ("$prev$cur")}.toInt()
            val taskTimeFinal = taskTimeToList.reduce{ prev, cur -> ("$prev$cur")}.toInt()
            // Return as a float; integer is too big
            return@sortBy ("$taskDateFinal.$taskTimeFinal").toFloat()
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
    private fun formatDayOfWeek(dayOfWeek: Int): String {
        // TODO: Choose start of week based on user's setting
        val startOfWeek = 1

        // -1 to map week's start as Monday to Calendar's start as Sunday
        val weekOffset = -1
        return week[(startOfWeek + dayOfWeek + weekOffset - 1) % 7]
    }
}