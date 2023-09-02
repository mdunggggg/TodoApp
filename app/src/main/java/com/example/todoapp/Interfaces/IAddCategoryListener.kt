package com.example.todoapp.Interfaces

import com.example.todoapp.Model.Category

interface IAddCategoryListener {
    fun onAddCategory(category: Category)
}