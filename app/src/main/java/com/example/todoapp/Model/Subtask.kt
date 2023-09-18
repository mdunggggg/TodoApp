package com.example.todoapp.Model

import java.io.Serializable

data class Subtask(
    var title: String,
    var isFinish : Boolean = false
) : Serializable