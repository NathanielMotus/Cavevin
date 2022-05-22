package com.nathaniel.motus.cavevin.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nathaniel.motus.cavevin.data.CellarEntry
import com.nathaniel.motus.cavevin.data.CellarItem
import com.nathaniel.motus.cavevin.data.cellar_database.CellarDatabase
import com.nathaniel.motus.cavevin.databinding.CellarItemViewBinding

class CellarItemAdapter(
) : ListAdapter<CellarItem, CellarItemAdapter.CellarItemViewHolder>(DiffCallBack) {

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<CellarItem>() {
            override fun areItemsTheSame(oldItem: CellarItem, newItem: CellarItem): Boolean {
                return oldItem.bottleId == newItem.bottleId
            }

            override fun areContentsTheSame(oldItem: CellarItem, newItem: CellarItem): Boolean {
                return (areItemsTheSame(
                    oldItem,
                    newItem
                ) and (oldItem.quantity == newItem.quantity)
                        and (oldItem.appellation == newItem.appellation)
                        and (oldItem.domain == newItem.domain)
                        and (oldItem.vintage == newItem.vintage)
                        and (oldItem.quantity == newItem.quantity))
            }
        }
    }

    class CellarItemViewHolder(private val binding: CellarItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cellarItem: CellarItem){
            binding.cellarItemViewAppellationText.text=cellarItem.appellation
            binding.cellarItemViewDomainText.text=cellarItem.domain
            binding.cellarItemViewCuveeText.text=cellarItem.cuvee
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellarItemViewHolder {
        val viewHolder=CellarItemViewHolder(CellarItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: CellarItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}