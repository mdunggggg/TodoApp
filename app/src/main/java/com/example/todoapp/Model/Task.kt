package com.example.todoapp.Model

import java.util.Date

data class Task(
    var id : Int,
    var title: String,
    var content : String,
    var subtasks : MutableList<Subtask>,
    var dateCreated : String,
    var dueDate: String,
    var dueTime: String,
    var categoryId: Int,
    var isFinish: Boolean = false
)