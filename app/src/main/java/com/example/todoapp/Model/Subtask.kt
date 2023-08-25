package com.example.todoapp.Model

import java.util.Date

data class Subtask(
    var id : Int,
    var title: String,
    var isFinish : Boolean = false
)