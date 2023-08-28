package com.example.todoapp.Interfaces

import com.example.todoapp.Model.Task

interface IItemTaskListener {
    fun onClickItemTask(task : Task)
}