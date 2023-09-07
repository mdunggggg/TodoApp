package com.example.todoapp.Adapter.RecyclerViewAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Model.CategoryAndTask
import com.example.todoapp.databinding.ItemCategoryStatisticsBinding

class ItemCategoryStatisticAdapter() : ListAdapter<CategoryAndTask, ItemCategoryStatisticAdapter.ItemCategoryStatisticViewHolder>(DiffCallback) {
    inner class ItemCategoryStatisticViewHolder(private val binding : ItemCategoryStatisticsBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(categoryAndTask: CategoryAndTask){
            binding.apply {
                tvCategoryName.text = categoryAndTask.titleCategory
                tvTotalFinishedTask.text = categoryAndTask.totalTaskFinish.toString()
                tvTotalTask.text = categoryAndTask.totalTask.toString()
                cvCategoryColor.setCardBackgroundColor(categoryAndTask.color)
                if(categoryAndTask.totalTask == 0){
                    tvPercentTask.text = "0"
                }
                else{
                    tvPercentTask.text = "${categoryAndTask.totalTaskFinish * 100 / categoryAndTask.totalTask}"
                }
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemCategoryStatisticViewHolder {
           return ItemCategoryStatisticViewHolder(ItemCategoryStatisticsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }



    override fun onBindViewHolder(holder: ItemCategoryStatisticViewHolder, position: Int) {
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