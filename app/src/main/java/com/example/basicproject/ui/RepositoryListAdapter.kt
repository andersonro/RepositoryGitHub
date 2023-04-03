package com.example.basicproject.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.basicproject.data.model.Repositories
import com.example.basicproject.databinding.ItemRepositoryBinding

class RepositoryListAdapter :
    ListAdapter<Repositories, RepositoryListAdapter.ViewModel>(DiffCallback()) {

    var repositoryShare: (Repositories) -> Unit = {}
    var repositoryCardView: ((Repositories) -> Unit)? = {}

    inner class ViewModel(private val binding: ItemRepositoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Repositories) {
            binding.tvNameRepository.text = item.name
            binding.tvLanguage.text = item.language

            binding.ivShare.setOnClickListener {
                repositoryShare(item)
            }


            binding.cvItem.setOnLongClickListener {
                repositoryCardView?.invoke(item)
                return@setOnLongClickListener true
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRepositoryBinding.inflate(inflater, parent, false)
        return ViewModel(binding)
    }

    override fun onBindViewHolder(holder: ViewModel, position: Int) {
        holder.bind(getItem(position))
    }
}

class DiffCallback : DiffUtil.ItemCallback<Repositories>() {
    override fun areItemsTheSame(oldItem: Repositories, newItem: Repositories) = oldItem == newItem
    override fun areContentsTheSame(oldItem: Repositories, newItem: Repositories) = oldItem.id === newItem.id

}