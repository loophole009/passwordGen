package com.example.passwordgen.ui.locker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.passwordgen.databinding.LockerItemBinding
import com.example.passwordgen.models.Locker
import com.example.passwordgen.util.Helper

class LockerAdapter(private val onLockerClicked: (Locker) -> Unit) :
    ListAdapter<Locker, LockerAdapter.LockerViewHolder>(ComparatorDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LockerViewHolder {
        val binding = LockerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LockerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LockerViewHolder, position: Int) {
        val locker = getItem(position)
        locker?.let {
            holder.bind(it)
        }
    }


    inner class LockerViewHolder(private val binding: LockerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(locker: Locker) {
            binding.website.text = locker.website
            binding.progressBar.progress = Helper.getProgress(locker.timestamp)
            binding.progressBar.max = 120

            binding.root.setOnClickListener {
                onLockerClicked(locker)
            }
        }

    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<Locker>() {
        override fun areItemsTheSame(oldItem: Locker, newItem: Locker): Boolean {
            return oldItem.website == newItem.website
        }

        override fun areContentsTheSame(oldItem: Locker, newItem: Locker): Boolean {
            return oldItem == newItem
        }
    }
}