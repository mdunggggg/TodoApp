package com.example.todoapp.Database.Category

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.todoapp.Database.TodoDatabase
import com.example.todoapp.Model.Category
import com.example.todoapp.Model.Relation.CategoryWithTasks

class CategoryRepository(application: Application) {
    private val categoryDao: CategoryDao =
        Room.databaseBuilder(application, TodoDatabase::class.java, "DatabaseVersion3")
            .allowMainThreadQueries().build().categoryDao()
        suspend fun insertCategory(category: Category)
            = categoryDao.insertCategory(category)
        suspend fun deleteCategory(category: Category)
            = categoryDao.deleteCategory(category)
        suspend fun updateCategory(category: Category)
            = categoryDao.updateCategory(category)
        suspend fun clearCategory()
            = categoryDao.clearCategory()

        fun getAllCategory() : LiveData<List<Category>>
            = categoryDao.getAllCategory()
        fun getCategoryByTitle(titleCategory: String)
            = categoryDao.getCategoryByTitle(titleCategory)
        fun getCategoryWithTasks() : LiveData<List<CategoryWithTasks>>
            = categoryDao.getCategoryWithTasks()

}