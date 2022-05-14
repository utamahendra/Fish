package com.example.fish.presentation.fishlist

import android.os.Bundle
import com.example.fish.R
import com.example.fish.common.ErrorCode
import com.example.fish.common.base.BaseActivity
import com.example.fish.common.extension.gone
import com.example.fish.common.extension.showIfNotAdded
import com.example.fish.common.extension.visible
import com.example.fish.common.viewbinding.viewBinding
import com.example.fish.common.viewstate.ListViewState
import com.example.fish.databinding.ActivityFishListBinding
import com.example.fish.domain.model.FilterData
import com.example.fish.domain.model.FishData
import com.example.fish.presentation.filter.FishFilterBottomSheetFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FishListActivity : BaseActivity() {

    private val binding by viewBinding(ActivityFishListBinding::inflate)

    private val viewModel by viewModel<FishListViewModel>()

    private val adapter by lazy { FishAdapter() }

    private lateinit var fishFilterBottomSheetFragment: FishFilterBottomSheetFragment

    private lateinit var fishData: List<FishData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        initListener()
        startObservingData()
        viewModel.getFishes()
        viewModel.getFilterData()
    }

    private fun initView() {
        binding.rvFishs.adapter = adapter
    }

    private fun initListener() {
        with(binding) {
            srlFishs.apply {
                setOnRefreshListener {
                    isRefreshing = false
                    adapter.clear()
                    viewModel.getFishes()
                }
            }
            btnFilter.setOnClickListener {
                fishFilterBottomSheetFragment.showIfNotAdded(
                    supportFragmentManager,
                    FishFilterBottomSheetFragment.TAG
                )
            }
        }
    }

    private fun startObservingData() {
        viewModel.fishesState.observe(this) { state ->
            when (state) {
                is ListViewState.Loading -> {
                    hideError()
                    showLoading()
                }

                else -> {
                    hideLoading()
                    when (state) {
                        is ListViewState.Success -> bindView(state.data)
                        is ListViewState.Error -> showError(state.viewError?.errorCode.toString()) { viewModel.getFishes() }
                        is ListViewState.EmptyData -> showEmpty()
                    }
                }
            }
        }

        viewModel.filterState.observe(this) { state ->
            when (state) {
                is ListViewState.Success -> setDataFilter(state.data)
            }
        }

    }

    private fun showLoading() {
        with(binding) {
            srlFishs.gone()
            sflFishs.apply {
                visible()
                startShimmer()
            }
        }
    }

    private fun hideLoading() {
        with(binding) {
            sflFishs.apply {
                gone()
                stopShimmer()
            }
            srlFishs.visible()
        }
    }

    private fun showError(errorCode: String, action: () -> Unit) {
        with(binding) {
            srlFishs.gone()
            when (errorCode) {
                ErrorCode.GLOBAL_INTERNET_ERROR -> {
                    binding.viewError.tvTitle.text = getString(R.string.connection_error)
                }

                else -> {
                    binding.viewError.tvTitle.text = getString(R.string.internal_server_error)
                }
            }
            viewError.btnRetry.visible()
            viewError.btnRetry.setOnClickListener { action.invoke() }
            viewError.viewErrorContainer.visible()
        }
    }

    private fun showEmpty() {
        with(binding) {
            srlFishs.gone()
            viewError.tvTitle.text = getString(R.string.empty_data)
            viewError.btnRetry.gone()
            viewError.viewErrorContainer.visible()
        }
    }

    private fun hideError() {
        with(binding) {
            srlFishs.visible()
            viewError.viewErrorContainer.gone()
        }
    }

    private fun bindView(data: List<FishData>) {
        fishData = data
        adapter.resetData(fishData)
        binding.btnFilter.visible()
    }

    private fun setDataFilter(filterData: FilterData) {
        fishFilterBottomSheetFragment = FishFilterBottomSheetFragment.newInstance(filterData)
        fishFilterBottomSheetFragment.setListener {
            var result = fishData
            if(fishFilterBottomSheetFragment.city.isNotEmpty()) {
               result = fishData.filter {
                    fishFilterBottomSheetFragment.city.contains(it.cityArea)
                }
            }

            if(fishFilterBottomSheetFragment.province.isNotEmpty()) {
                result = if (result.isNotEmpty()) {
                    result.filter {
                        fishFilterBottomSheetFragment.province.contains(it.provinceArea)
                    }
                } else {
                    fishData.filter {
                        fishFilterBottomSheetFragment.province.contains(it.provinceArea)
                    }
                }
            }

            if(fishFilterBottomSheetFragment.size.isNotEmpty()) {
                result = if (result.isNotEmpty()) {
                    result.filter {
                        fishFilterBottomSheetFragment.size.contains(it.size)
                    }
                } else {
                    fishData.filter {
                        fishFilterBottomSheetFragment.size.contains(it.size)
                    }
                }
            }
            if (result.isEmpty()) {
                showEmpty()
            } else {
                adapter.resetData(result)
            }
        }
    }
}