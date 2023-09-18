package com.example.todoapp.ViewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp.Database.Category.CategoryRepository
import com.example.todoapp.Model.Category
import com.example.todoapp.Model.Relation.CategoryWithTasks
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : ViewModel(){
    private val categoryRepository : CategoryRepository
        = CategoryRepository(application)
    fun insertCategory(category: Category) = viewModelScope.launch {
        categoryRepository.insertCategory(category)
    }
    fun deleteCategory(category: Category) = viewModelScope.launch {
        categoryRepository.deleteCategory(category)
    }
    fun updateCategory(category: Category) = viewModelScope.launch {
        categoryRepository.updateCategory(category)
    }
    fun clearCategory() = viewModelScope.launch {
        categoryRepository.clearCategory()
    }
    fun getAllCategory() = categoryRepository.getAllCategory()
    fun getCategoryByTitle(titleCategory: String) = categoryRepository.getCategoryByTitle(titleCategory)
    fun getListCategoryByTitle(title: String): LiveData<List<Category>>
        = categoryRepository.getListCategoryByTitle(title)
    fun getCategoryWithTasks(): LiveData<List<CategoryWithTasks>>
        = categoryRepository.getCategoryWithTasks()

    fun getCategoryWithTasksByTitle(titleCategory: String): CategoryWithTasks
        = categoryRepository.getCategoryWithTasksByTitle(titleCategory)
    class CategoryViewModelFactory(private val application: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(CategoryViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return CategoryViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}