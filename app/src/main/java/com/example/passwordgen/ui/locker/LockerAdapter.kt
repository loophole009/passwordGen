package com.example.passwordgen.ui.locker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.passwordgen.databinding.LockerItemBinding
import com.example.passwordgen.models.Locker
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.abs

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

    @SuppressLint("SimpleDateFormat")
    fun getProgress(dateInString: String?): Int {
        val d1: Date
        val d2: Date
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        d1 = sdf.parse(sdf.format(Date())) as Date
        d2 = dateInString?.let { sdf.parse(it) } as Date
        val difference = abs(d1.time - d2.time)
        val differenceDates = difference / (24 * 60 * 60 * 1000)
        return Math.toIntExact(differenceDates)
    }

    inner class LockerViewHolder(private val binding: LockerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(locker: Locker) {
            binding.website.text = locker.website
            binding.progressBar.progress = getProgress(locker.timestamp)
            binding.progressBar.max = 90

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