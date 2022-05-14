package com.example.fish.presentation.fishlist

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.fish.common.base.BaseAdapter
import com.example.fish.common.base.BaseHolder
import com.example.fish.common.extension.formatCurrency
import com.example.fish.databinding.ItemFishBinding
import com.example.fish.domain.model.FishData

class FishAdapter : BaseAdapter<FishData?, FishAdapter.FishHolder>() {

    override fun bindViewHolder(holder: FishHolder, data: FishData?) {
        data?.let {
            holder.bind()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FishHolder {
        val itemBinding = ItemFishBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FishHolder(itemBinding)
    }

    inner class FishHolder(private val binding: ItemFishBinding) :
        BaseHolder<FishData?>(listener, binding.root) {
        fun bind() {
            item?.apply {
                binding.tvCommodity.text = commodity
                if (!cityArea.isNullOrBlank() && !provinceArea.isNullOrBlank()) {
                    binding.tvArea.text = "Area: $cityArea, $provinceArea"
                } else if (!cityArea.isNullOrBlank()) {
                    binding.tvArea.text = "Area: $cityArea"
                } else {
                    binding.tvArea.text = "Area: $provinceArea"
                }
                binding.tvSize.text = "Size: $size cm"
                binding.tvPrice.text = "Price: ${price.toDouble().formatCurrency()} "
            }
        }
    }
}