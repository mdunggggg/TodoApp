package com.example.todoapp.Database.Task

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.todoapp.Database.TodoDatabase
import com.example.todoapp.Model.Task

class TaskRepository(application: Application) {
    private var taskDao : TaskDao =
        Room.databaseBuilder(application, TodoDatabase::class.java, "MyDatabase").allowMainThreadQueries().build().taskDao()
    suspend fun insertTask(task: Task) = taskDao.insertTask(task)
    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)
    suspend fun updateTask(task: Task) = taskDao.updateTask(task)
    suspend fun clearTasks() = taskDao.clearTasks()
    fun getAllTasks() : LiveData<List<Task>> = taskDao.getAllTasks()
    fun getAllFinishTasks() : LiveData<List<Task>> = taskDao.getAllFinishTasks()
    fun getAllUnFinishTasks() : LiveData<List<Task>> = taskDao.getAllUnFinishTasks()

}