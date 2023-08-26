package com.example.todoapp.Adapter.RecyclerViewAdapter

import android.util.Log
import android.util.LogPrinter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Interfaces.ICategoryListener
import com.example.todoapp.Model.Category
import com.example.todoapp.databinding.ItemCategoryRvBinding

class CategoryAdapter(
    private val listCategory : List<Category>,
    private val categoryListener : ICategoryListener,
    private val onDismiss : () -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(private val binding : ItemCategoryRvBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(category: Category){
            binding.apply {
                tvCategoryName.text = category.title
                tvCategoryName.setTextColor(category.color)
                root.setOnClickListener {
                    categoryListener.onClickCategory(category.title)
                    onDismiss()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        Log.d("CategoryAdapter", "onCreateViewHolder: ")
        return CategoryViewHolder(ItemCategoryRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(listCategory[position])
    }
}