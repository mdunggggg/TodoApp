package com.example.todoapp.Model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date
@Entity(tableName = "task_table")
data class Task (
    @PrimaryKey(autoGenerate = true)
    var idTask : Long = 0,
    var title: String,
    var content : String = "",
    var subtasks : List<Subtask> = emptyList(),
    var dateCreated : String,
    var dueDate: String = "",
    var dueTime: String = "",
    var titleCategory: String,
    var isFinish: Boolean = false
) : Serializable