package com.example.todoapp.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
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
    var color : Int,
    var isFinish: Boolean = false,
    var isStored : Boolean = false
) : Serializable