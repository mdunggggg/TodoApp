package com.example.todoapp.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todoapp.Database.Category.CategoryDao
import com.example.todoapp.Database.Task.TaskDao
import com.example.todoapp.Model.Category
import com.example.todoapp.Model.Task

@Database(entities = [Category::class, Task::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase(){
    abstract fun taskDao() : TaskDao
    abstract val categoryDao : CategoryDao
    companion object{
        private const val DATABASE_NAME = "todo_db"
        private const val NUMBER_OF_THREADS = 4
        @Volatile
        private var INSTANCE : TodoDatabase? = null

        fun getInstance(context: Context) : TodoDatabase{
            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    DATABASE_NAME
                ).build().also { INSTANCE = it }
            }
        }
    }
}