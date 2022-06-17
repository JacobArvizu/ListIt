package com.jarvizu.listit.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jarvizu.listit.databinding.CardLayoutBinding

class CommentAdapter(val comments : List<String>) : RecyclerView.Adapter<CommentAdapter.ListViewHolder>() {

    inner class ListViewHolder(val binding: CardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.txtComment.text = "Comment:\n" + comments[position]
    }
}