package com.nathaniel.motus.cavevin.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nathaniel.motus.cavevin.R
import com.nathaniel.motus.cavevin.controller.CellarStorageUtils
import com.nathaniel.motus.cavevin.data.CellarItem
import com.nathaniel.motus.cavevin.data.cellar_database.Stock
import com.nathaniel.motus.cavevin.databinding.CellarItemViewBinding
import com.nathaniel.motus.cavevin.viewmodels.BottleListViewModel
import java.util.*

class CellarItemAdapter(val bottleListViewModel: BottleListViewModel) :
    ListAdapter<CellarItem, CellarItemAdapter.CellarItemViewHolder>(DiffCallBack) {

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

    class CellarItemViewHolder(
        private val binding: CellarItemViewBinding,
        private val viewModel: BottleListViewModel,
    ) : RecyclerView.ViewHolder(binding.root), StockView.Observer {

        private var cellarId = 0
        private var bottleId = 0
        private var alreadyObservesStock = false
        private var timer: Timer? = null

        fun bind(cellarItem: CellarItem) {
            cellarId = cellarItem.cellarId
            bottleId = cellarItem.bottleId
            binding.cellarItemViewAppellationText.text = cellarItem.appellation
            binding.cellarItemViewDomainText.text = cellarItem.domain
            binding.cellarItemViewCuveeText.text = cellarItem.cuvee
            binding.cellarItemViewRatingView.rating = cellarItem.rating
            binding.cellarItemViewRatingView.displayMode = RatingView.VIEW_MODE
            binding.cellarItemViewBottleNameText.text = cellarItem.bottleName
            binding.cellarItemViewCapacityText.text = "${cellarItem.capacity} L"
            binding.cellarItemViewVintageText.text = cellarItem.vintage
            binding.cellarItemViewWineView.wineColor=cellarItem.neutralWineColor
            binding.cellarItemViewWineView.wineStillness=cellarItem.neutralWineStillness
            setBottleImage(cellarItem.picture)
            if (!alreadyObservesStock) {
                binding.cellarItemStock.observe(this)
                alreadyObservesStock = true
            }
            binding.cellarItemStock.stock = cellarItem.quantity
        }

        private fun setBottleImage(imageName: String?) {
            if (imageName != null && imageName != "")
                binding.cellarItemViewPhotoImage.setImageBitmap(
                    CellarStorageUtils.getBitmapFromInternalStorage(
                        binding.root.context.filesDir,
                        binding.root.context.resources.getString(R.string.photo_folder_name),
                        imageName + binding.root.context.resources.getString(R.string.photo_thumbnail_suffix)
                    )
                )
            else
                binding.cellarItemViewPhotoImage.setImageDrawable(
                    AppCompatResources.getDrawable(
                        binding.root.context,
                        R.drawable.photo_frame
                    )
                )
        }


        override fun notifyNewStock(stock: Int) {
            //the timer is used to prevent update stock from db before db is updated by stockview
            //it waits 1 s before updating db
            timer?.cancel()

            timer = object : Timer() {}.apply {
                schedule(object : TimerTask() {
                    override fun run() {
                        viewModel.updateStock(Stock(cellarId, bottleId, stock))
                    }
                }, 1000)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellarItemViewHolder {
        return CellarItemViewHolder(
            CellarItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), bottleListViewModel
        )
    }

    override fun onBindViewHolder(holder: CellarItemViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            val action=CellarItemListFragmentDirections.actionCellarItemListFragmentToBottleFragment(getItem(position).bottleId)
            holder.itemView.findNavController().navigate(action)
        }
    }
}