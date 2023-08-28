package com.example.todoapp.Adapter.RecyclerViewAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.Model.Task
import com.example.todoapp.databinding.ItemTaskRvBinding

class HomeTaskAdapter(
        private val listTask : List<Task>

) : RecyclerView.Adapter<HomeTaskAdapter.HomeTaskViewHolder>() {
        inner class HomeTaskViewHolder(private val binding : ItemTaskRvBinding) : RecyclerView.ViewHolder(binding.root){
               fun bind(task : Task){
                      binding.apply {
                             tvTaskName.text = task.title
                             tvTaskTime.text = task.dueTime
                             tvTaskDescription.text = task.content

                      }
               }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTaskViewHolder {
                return HomeTaskViewHolder(ItemTaskRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun getItemCount(): Int {
                return listTask.size
        }

        override fun onBindViewHolder(holder: HomeTaskViewHolder, position: Int) {
                holder.bind(listTask[position])
        }
}