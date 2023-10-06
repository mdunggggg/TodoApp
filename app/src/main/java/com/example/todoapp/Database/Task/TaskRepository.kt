package com.example.todoapp.Database.Task

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.todoapp.Database.TodoDatabase
import com.example.todoapp.Model.Relation.CategoryWithTasks
import com.example.todoapp.Model.Task

class TaskRepository(application: Application) {
    private var taskDao : TaskDao =
        Room.databaseBuilder(application, TodoDatabase::class.java, "DatabaseVersion5").allowMainThreadQueries().build().taskDao()

    suspend fun insertTask(task: Task) = taskDao.insertTask(task)
    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)
    suspend fun clearTasksByCategory(titleCategory: String) = taskDao.clearTasksByCategory(titleCategory)
    suspend fun updateTask(task: Task) = taskDao.updateTask(task)
    suspend fun clearTasks() = taskDao.clearTasks()
    fun getAllDeletedTasks(): LiveData<List<Task>> = taskDao.getAllDeletedTasks()
    fun getAllTasksByCategory(titleCategory: String): LiveData<List<Task>> = taskDao.getAllTasksByCategory(titleCategory)
    fun getAllTasksInRange(date: String): LiveData<List<Task>> = taskDao.getAllTasksInRange(date)
    fun getAllTasksByDate(date: String): LiveData<List<Task>> = taskDao.getAllTasksByDate(date)
    fun getAllTasks() : LiveData<List<Task>> = taskDao.getAllTasks()
    fun getTaskByTitle(title: String) : LiveData<List<Task>> = taskDao.getTaskByTitle(title)
    fun getAllTasksOrderByFinish() : LiveData<List<Task>> = taskDao.getAllTasksOrderByFinish()
    fun getAllFinishTasks() : LiveData<List<Task>> = taskDao.getAllFinishTasks()
    fun getAllUnFinishTasks() : LiveData<List<Task>> = taskDao.getAllUnFinishTasks()
    fun getCategoryWithTasks() : LiveData<List<CategoryWithTasks>>
            = taskDao.getCategoryWithTasks()

}