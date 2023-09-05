package com.example.todoapp.ViewModel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.Model.Subtask
import com.example.todoapp.Model.Task

class DetailTaskViewModel(private val task : Task) : ViewModel() {
    var newTitle = task.title
    var newDescription = task.content
    var newDueDate = task.dueDate
    var newDueTime = task.dueTime
    var newIsFinished = task.isFinish
    private var newSubtasks = task.subtasks.toMutableList()
    private val subTasks = MutableLiveData<List<Subtask>>()
    val _subTasks
        get() = subTasks

    fun isChanged() : Boolean{

        return newTitle != task.title ||
                newDescription != task.content ||
                newDueDate != task.dueDate ||
                newDueTime != task.dueTime ||
                newIsFinished != task.isFinish ||
                subTasksChanged()
    }
    private fun subTasksChanged() : Boolean{
        if(newSubtasks.size != task.subtasks.size)
            return true
        for(i in newSubtasks.indices){
            if((newSubtasks[i].title != task.subtasks[i].title) || (newSubtasks[i].isFinish != task.subtasks[i].isFinish))
                return true
        }
        return false
    }
    fun getNewTask() : Task{
        return task.copy(
            title = newTitle,
            content = newDescription,
            dueDate = newDueDate,
            dueTime = newDueTime,
            subtasks = newSubtasks,
            isFinish = newIsFinished
        )
    }
    fun addSubtask(subtask: Subtask){
        newSubtasks.add(subtask)
        subTasks.value = newSubtasks
    }
    fun onUpdatedSubtask(position: Int){
        val isFinish = !newSubtasks[position].isFinish
        newSubtasks[position] = newSubtasks[position].copy(isFinish = isFinish )
        subTasks.value = newSubtasks
    }
    fun onRemoveSubtask(position: Int){
        newSubtasks.removeAt(position)
        subTasks.value = newSubtasks
    }
}
class DetailTaskViewModelFactory(private val task: Task) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DetailTaskViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return DetailTaskViewModel(task) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}