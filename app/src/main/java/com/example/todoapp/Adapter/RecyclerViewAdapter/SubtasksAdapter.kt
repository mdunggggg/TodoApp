package com.example.todoapp.Adapter.RecyclerViewAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Model.Subtask
import com.example.todoapp.databinding.ItemSubtaskRvBinding

class SubtasksAdapter(
    private val onUpdate : (Int) -> Unit,
    private val onRemove : (Int) -> Unit
) : ListAdapter<Subtask, SubtasksAdapter.SubtasksViewHolder>(DiffCallback) {

    inner class SubtasksViewHolder(private val binding : ItemSubtaskRvBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(subtask: Subtask){
            binding.apply {
                tvSubtaskName.text = subtask.title
                cbSubtaskStatus.isChecked = subtask.isFinish
                cbSubtaskStatus.setOnClickListener {
                    onUpdate(absoluteAdapterPosition)
                }
                ivRemoveSubtask.setOnClickListener {
                    onRemove(absoluteAdapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtasksViewHolder {
        return SubtasksViewHolder(ItemSubtaskRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    fun updateData(list : List<Subtask>){
        submitList(list.toList())
    }

    override fun onBindViewHolder(holder: SubtasksViewHolder, position: Int) {
        val current = getItem(position)
        Log.d("SubtaskAdapter", "onBindViewHolder: ${current.title}")
        holder.bind(current)
    }
    companion object{
        private val DiffCallback = object : DiffUtil.ItemCallback<Subtask>(){
            override fun areItemsTheSame(oldItem: Subtask, newItem: Subtask): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: Subtask, newItem: Subtask): Boolean {
                return (oldItem.isFinish == newItem.isFinish) && (oldItem.title == newItem.title)
            }
        }
    }
}