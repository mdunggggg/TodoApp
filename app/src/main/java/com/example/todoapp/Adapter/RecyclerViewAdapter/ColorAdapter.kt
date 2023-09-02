package com.example.todoapp.Adapter.RecyclerViewAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ColorCircleBinding

class ColorAdapter(
    private val colors : List<Int>,
    private val onColorClicked : (Int) -> Unit,
    private val onDismiss : () -> Unit
) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>(){
    inner class ColorViewHolder(private val binding : ColorCircleBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.apply {
                color.backgroundTintList = itemView.context.getColorStateList(colors[position])
                root.setOnClickListener {
                    onColorClicked(colors[position])
                    onDismiss()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        return ColorViewHolder(ColorCircleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return  colors.size
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(position)
    }
}