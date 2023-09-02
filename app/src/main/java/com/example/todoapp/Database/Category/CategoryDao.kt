package com.example.todoapp.Database.Category

import androidx.lifecycle.LiveData
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
     suspend fun insertCategory(category: Category)
    @Delete
    suspend  fun deleteCategory(category: Category)
    @Update
    suspend fun updateCategory(category: Category)
    @Query("DELETE FROM category_table")
    suspend fun clearCategory()
    @Query("SELECT * FROM category_table")
    fun getAllCategory(): LiveData<List<Category>>

    @Transaction
    @Query("SELECT * FROM category_table")
    fun getCategoryWithTasks(): LiveData<List<CategoryWithTasks>>


}