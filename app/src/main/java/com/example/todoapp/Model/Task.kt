package com.example.todoapp.Model

import java.util.Date

data class Task(
    var id : Int,
    var title: String,
    var content : String,
    var subtasks : MutableList<Subtask>,
    var dateCreated : Date,
    var dueDate: Date,
    var dueTime: Date,
    var isFinish: Boolean = false
)