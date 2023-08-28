package com.example.todoapp.Database.Category

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.todoapp.Model.Category
import com.example.todoapp.Model.Relation.CategoryWithTasks
@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertCategory(category: Category)
    @Delete
    fun deleteCategory(category: Category)
    @Update
     fun updateCategory(category: Category)
    @Query("SELECT * FROM category_table")
    fun getAllCategory(): List<Category>

    @Transaction
    @Query("SELECT * FROM category_table")
    fun getCategoryWithTasks(): List<CategoryWithTasks>


}