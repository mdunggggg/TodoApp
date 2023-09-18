package com.example.todoapp.ViewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp.Database.Task.TaskRepository
import com.example.todoapp.Model.Task
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : ViewModel() {
    private val taskRepository : TaskRepository = TaskRepository(application)
    private lateinit var tasks: MutableList<Task>

    fun insertTask(task: Task) = viewModelScope.launch {
        taskRepository.insertTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        taskRepository.deleteTask(task)
    }
    fun updateTask(task: Task) = viewModelScope.launch {
        taskRepository.updateTask(task)
    }
    fun getAllDeletedTasks(): LiveData<List<Task>> = taskRepository.getAllDeletedTasks()
    fun getAllTasksByDate(date: String): LiveData<List<Task>> = taskRepository.getAllTasksByDate(date)
    fun clearTasks() = viewModelScope.launch {
        taskRepository.clearTasks()
    }
    fun getAllTasksByCategory(titleCategory: String): LiveData<List<Task>> = taskRepository.getAllTasksByCategory(titleCategory)
    fun getTaskByTitle(title: String) : LiveData<List<Task>> = taskRepository.getTaskByTitle(title)
    fun getAllTasks() : LiveData<List<Task>> = taskRepository.getAllTasks()
    fun getAllTasksInRange(date: String): LiveData<List<Task>> = taskRepository.getAllTasksInRange(date)
    fun getAllTasksOrderByFinish() : LiveData<List<Task>> = taskRepository.getAllTasksOrderByFinish()
    fun getAllFinishTasks() : LiveData<List<Task>> = taskRepository.getAllFinishTasks()
    fun getAllUnFinishTasks() : LiveData<List<Task>> = taskRepository.getAllUnFinishTasks()
    fun getCategoryWithTasks() = taskRepository.getCategoryWithTasks()

    class TaskViewModelFactory(private val application: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(TaskViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return TaskViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }




}