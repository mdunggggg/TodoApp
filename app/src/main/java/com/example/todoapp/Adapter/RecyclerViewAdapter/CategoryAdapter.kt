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
import com.example.todoapp.databinding.ItemCategoryRvBinding

class CategoryAdapter(
    private val categoryListener : ICategoryListener,
    private val onDismiss : () -> Unit
) : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(DiffCallback()) {

    inner class CategoryViewHolder(private val binding : ItemCategoryRvBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(category: Category){
            binding.apply {
                tvCategoryName.apply {
                    text = category.title
                    setTextColor(ContextCompat.getColor(itemView.context, category.color))
                }
                root.setOnClickListener {
                    categoryListener.onClickCategory(category.title)
                    onDismiss()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        return CategoryViewHolder(ItemCategoryRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }



    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }
    companion object{
        class DiffCallback : androidx.recyclerview.widget.DiffUtil.ItemCallback<Category>(){
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.idCategory == newItem.idCategory
            }

        }
    }
}