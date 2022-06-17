package com.jarvizu.listit.data

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.jarvizu.listit.R
import com.jarvizu.listit.databinding.ItemLayoutBinding
import com.jarvizu.listit.ui.main.MainFragmentDirections
import com.jarvizu.listit.utils.Constants

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    private var titles = mutableListOf<String>()
    private var subs = mutableListOf<String>()
    private var images = mutableListOf<String>()
    private var permaLinks = mutableListOf<String>()

    inner class ListViewHolder(val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            differ.currentList.let {
                for (child in it) {
                    titles.add(child.data.title)
                    subs.add("/r/" + child.data.subreddit)
                    images.add(child.data.thumbnail)
                    permaLinks.add(Constants.BASE_URL + child.data.permalink)
                }
            }
            binding.cardLayout.setOnClickListener {
                Toast.makeText(
                    binding.root.context,
                    permaLinks[adapterPosition],
                    Toast.LENGTH_SHORT
                ).show()
                val selfPost = differ.currentList[adapterPosition].data.selftext
                val directions = MainFragmentDirections.actionMainFragmentToPostFragment(
                    titles[adapterPosition],
                    subs[adapterPosition],
                    permaLinks[adapterPosition],
                    selfPost
                )
                binding.root.findNavController().navigate(directions)
            }
        }
    }

    // Diff optimization for ListObject response
    private val diffCallback = object : DiffUtil.ItemCallback<ListObject.Data.Children>() {
        override fun areItemsTheSame(
            oldItem: ListObject.Data.Children,
            newItem: ListObject.Data.Children
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ListObject.Data.Children,
            newItem: ListObject.Data.Children
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(listObject: List<ListObject.Data.Children>) {
        differ.submitList(listObject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.postTitle.text = titles[position]
        holder.binding.postSubreddit.text = subs[position]
        if (images[position] == "self") {
            holder.binding.ivImage.load(R.drawable.baseline_person_24)
        } else {
            holder.binding.ivImage.load(images[position])
        }
    }

}