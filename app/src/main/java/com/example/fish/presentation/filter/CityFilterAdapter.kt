package com.example.fish.presentation.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.fish.common.base.BaseAdapter
import com.example.fish.common.base.BaseHolder
import com.example.fish.databinding.ItemFilterBinding
import com.example.fish.domain.model.FilterListData

class CityFilterAdapter : BaseAdapter<FilterListData, CityFilterAdapter.CityHolder>() {

    override fun bindViewHolder(holder: CityHolder, data: FilterListData?) {
        data?.let {
            holder.bind()
            holder.initListener()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val itemBinding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityHolder(itemBinding)
    }

    inner class CityHolder(private val binding: ItemFilterBinding) :
        BaseHolder<FilterListData>(listener, binding.root) {
        fun bind() {
            item?.let {
                binding.cbName.text = it.name
                binding.cbName.isChecked = it.isChecked
            }
        }

        fun initListener() {
            binding.cbName.setOnCheckedChangeListener { view, _ ->
                listener(adapterPosition, item, view.id)
            }
        }
    }
}