package com.example.todoapp.Model

import java.io.Serializable
import java.util.Date

data class Subtask(
    var id : Long,
    var title: String,
    var isFinish : Boolean = false
)