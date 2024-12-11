package com.cs407.groupproject407

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar
import java.util.GregorianCalendar

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
        val toRemove = mutableListOf<Int>()

        for (i in 0..< userData.length()) {
            val currTask = JSONObject(userData.getString(i))

            val currDate = currTask.getString("date")
            val currTime = currTask.getString("time")

            // Collect tasks to remove
            if (calcIsOld(currDate, currTime)) {
                toRemove.add(i)
                continue
            }

            val currId = currTask.getInt("id")
            val currType = currTask.getString("activityType")
            val currTitle = currTask.getString("activityName")

            val dayOfWeekTemp = ""
            CoroutineScope(Dispatchers.IO).launch {
                val currDayOfWeek = calcDayOfWeek(currDate)
                val dayOfWeekFormatted = formatDayOfWeek(currDayOfWeek)
                // Ensure task has been added to list first
                while (taskList.size <= i) {
                    continue
                }
                taskList[i].dayOfWeek = dayOfWeekFormatted
            }

            val currRecurring = currTask.getBoolean("recurring")
            val currInfo = currTask.getString("notes")

            taskList.add(TaskSummary(currId, currType, currTitle, currDate, currTime, dayOfWeekTemp, currRecurring, currInfo))
        }

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            removeOldTasks(sharedPref, toRemove)
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
            // Sort by date, then time
            return@sortBy ("$taskDateFinal.$taskTimeFinal")
        }
    }

    // Get day of week as int, given date
    private suspend fun calcDayOfWeek(date: String): Int {
        return withContext(Dispatchers.Default) {
            val splitDate = date.split("-")

            // Get day of week from Calendar
            val day = splitDate[2].toInt()
            val month = splitDate[1].toInt() - 1 // 0 indexed
            val year = splitDate[0].toInt()

            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            return@withContext calendar.get(Calendar.DAY_OF_WEEK)
        }
    }

    // Convert int value to day of week string
    private fun formatDayOfWeek(dayOfWeek: Int): String {
        // TODO: Choose start of week based on user's setting
        val startOfWeek = 1

        // -1 to map week's start as Monday to Calendar's start as Sunday
        val weekOffset = -1
        return week[(startOfWeek + dayOfWeek + weekOffset - 1) % 7]
    }

    // Calculate if the task's due date has passed
    private fun calcIsOld(date: String, time: String): Boolean {
        // Get day of week from Calendar
        val splitDate = date.split("-")
        val splitTime = time.split(":")

        val day = splitDate[2].toInt()
        val month = splitDate[1].toInt() - 1 // 0 indexed
        val year = splitDate[0].toInt()
        val hour = splitTime[0].toInt()
        val minute = splitTime[1].toInt()

        // Set priority
        val compareTime = GregorianCalendar(year, month, day, hour, minute)
        return Calendar.getInstance().time.after(compareTime.time)
    }

    // Remove tasks whose due dates have passed
    private suspend fun removeOldTasks(sharedPref: SharedPreferences, toRemove: MutableList<Int>) {
        withContext(Dispatchers.IO) {
            val userData = JSONArray(sharedPref.getString("userData", ""))
            var modifiedToRemove = listOf(toRemove)[0]

            for (i in 0..<toRemove.size) {
                userData.remove(modifiedToRemove[i])
                modifiedToRemove = modifiedToRemove.map { it - 1 }.toMutableList() // Shift indexes
            }

            val editor = sharedPref.edit()
            editor.remove("userData")
            editor.putString("userData", userData.toString())
            editor.apply()
        }
    }


}