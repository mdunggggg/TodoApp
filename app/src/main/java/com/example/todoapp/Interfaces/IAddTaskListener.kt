package com.example.todoapp.Interfaces

import com.example.todoapp.Model.Task

interface IAddTaskListener {
    fun onAddTask(task : Task)
}