package com.example.fish.presentation.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.fish.common.base.BaseAdapter
import com.example.fish.common.base.BaseHolder
import com.example.fish.databinding.ItemFilterBinding
import com.example.fish.domain.model.FilterListData

class ProvinceFilterAdapter : BaseAdapter<FilterListData, ProvinceFilterAdapter.ProvinceHolder>() {

    override fun bindViewHolder(holder: ProvinceHolder, data: FilterListData?) {
        data?.let {
            holder.bind()
            holder.initListener()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProvinceHolder {
        val itemBinding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProvinceHolder(itemBinding)
    }

    inner class ProvinceHolder(private val binding: ItemFilterBinding) :
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