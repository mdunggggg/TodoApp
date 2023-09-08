package com.example.todoapp.Adapter.RecyclerViewAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Model.CategoryAndTask
import com.example.todoapp.Utils.ColorUtils
import com.example.todoapp.databinding.ItemCategoryHomeBinding

class CategoryHomeAdapter(
    private val onItemClick : (String) -> Unit
) : ListAdapter<CategoryAndTask, CategoryHomeAdapter.CategoryHomeViewHolder>(DiffCallback){
    inner class CategoryHomeViewHolder(private val binding : ItemCategoryHomeBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(categoryAndTask: CategoryAndTask){
            binding.apply {
                tvCategoryName.text = categoryAndTask.titleCategory
                tvCategoryProjectCount.text = categoryAndTask.totalTask.toString() + " Projects"
                if(categoryAndTask.totalTask == 0){
                    tvProgressPercent.text = "0%"
                    progressBar.progress = 0
                }
                else{
                    tvProgressPercent.text = "${categoryAndTask.totalTaskFinish * 100 / categoryAndTask.totalTask}" + "%"
                    progressBar.progress = categoryAndTask.totalTaskFinish * 100 / categoryAndTask.totalTask
                }
                progressBar.trackColor = ColorUtils.getColorTransparent(categoryAndTask.color)
                progressBar.setIndicatorColor(categoryAndTask.color)
               llCategoryName.setBackgroundColor(ColorUtils.getColorTransparent(categoryAndTask.color))
                root.setOnClickListener {
                    onItemClick(categoryAndTask.titleCategory)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHomeViewHolder {
        return CategoryHomeViewHolder(ItemCategoryHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CategoryHomeViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }
    companion object{
        private val DiffCallback = object : DiffUtil.ItemCallback<CategoryAndTask>(){
            override fun areItemsTheSame(
                oldItem: CategoryAndTask,
                newItem: CategoryAndTask
            ): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(
                oldItem: CategoryAndTask,
                newItem: CategoryAndTask
            ): Boolean {
                return oldItem.titleCategory == newItem.titleCategory ||
                        oldItem.totalTask == newItem.totalTask ||
                        oldItem.totalTaskFinish == newItem.totalTaskFinish
            }
        }
    }
}