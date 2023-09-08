package com.example.todoapp.Database.Task

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.todoapp.Model.Relation.CategoryWithTasks
import com.example.todoapp.Model.Task
@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task) : Long
    @Delete
    suspend fun deleteTask(task: Task)
    @Update
     suspend fun updateTask(task: Task)
    @Query("DELETE FROM task_table")
    suspend fun clearTasks()
    @Query("SELECT * FROM task_table")
    fun getAllTasks(): LiveData<List<Task>>
    @Query("SELECT * FROM task_table ORDER BY isFinish ASC")
    fun getAllTasksOrderByFinish(): LiveData<List<Task>>
    @Query("SELECT * FROM task_table WHERE dueDate= :date AND isFinish = 0 ORDER BY dueTime ASC" )
    fun getAllTasksByDate(date: String): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE isFinish = 1")
    fun getAllFinishTasks(): LiveData<List<Task>>
    @Query("SELECT * FROM task_table WHERE isFinish = 0")
    fun getAllUnFinishTasks(): LiveData<List<Task>>

    @Transaction
    @Query("SELECT * FROM category_table")
    fun getCategoryWithTasks(): LiveData<List<CategoryWithTasks>>


}