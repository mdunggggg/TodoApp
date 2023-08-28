package com.example.todoapp.Model.Relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.todoapp.Model.Category
import com.example.todoapp.Model.Task

data class CategoryWithTasks(
    @Embedded
    val category: Category,
    @Relation(
        parentColumn = "idCategory",
        entityColumn = "idCategory"
    )
    val tasks: List<Task>
)