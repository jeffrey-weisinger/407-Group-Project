package com.cs407.groupproject407

class TaskSummary (
    var taskID: Int = 0,
    var taskType: String = "School",
    var taskTitle: String = "Example Title",
    var taskDate: String = "YYYY-MM-DD",
    var taskTime: String = "XX:XX",
    var dayOfWeek: String = "Monday",
    var isRecurring: Boolean = false,
    var taskInfo: String = "This is an example task."
){
}