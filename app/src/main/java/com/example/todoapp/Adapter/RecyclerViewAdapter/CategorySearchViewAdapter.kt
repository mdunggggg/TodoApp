package com.example.todoapp.Adapter.RecyclerViewAdapter

import android.util.Log
import android.util.LogPrinter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Interfaces.ICategoryListener
import com.example.todoapp.Model.Category
import com.example.todoapp.Utils.ColorUtils
import com.example.todoapp.databinding.ItemCategoryRvBinding
import com.example.todoapp.databinding.ItemCategorySearchViewBinding
import com.example.todoapp.databinding.ItemTaskSearchViewBinding

class CategorySearchViewAdapter(
    private val onCountTask : (String) -> String,
    private val onItemClick : (String) -> Unit
) : ListAdapter<Category, CategorySearchViewAdapter.CategorySearchViewViewHolder>(DiffCallback()) {

    inner class CategorySearchViewViewHolder(private val binding : ItemCategorySearchViewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(category: Category){
            binding.apply {
                cvCategoryColorSearchView.setCardBackgroundColor(category.color)
                tvCategoryNameSearchView.text = category.titleCategory
                tvTasksCountNumber.text = onCountTask(category.titleCategory)
                root.setOnClickListener{
                    onItemClick(category.titleCategory)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategorySearchViewViewHolder {
        return CategorySearchViewViewHolder(ItemCategorySearchViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }



    override fun onBindViewHolder(holder: CategorySearchViewViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }
    companion object{
        class DiffCallback : androidx.recyclerview.widget.DiffUtil.ItemCallback<Category>(){
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.titleCategory == newItem.titleCategory
            }

        }
    }
}