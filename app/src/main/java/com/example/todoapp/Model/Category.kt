package com.example.todoapp.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "category_table")
data class Category(
    @PrimaryKey(autoGenerate = false)
    var titleCategory: String,
    var color : Int,
    var numTask : Int = 0
) : Serializable