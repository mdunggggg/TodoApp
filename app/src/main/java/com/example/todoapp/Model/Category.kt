package com.example.todoapp.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "category_table")
data class Category(
    @PrimaryKey(autoGenerate = true)
    var idCategory : Long,
    var title: String,
    var color : Int
)