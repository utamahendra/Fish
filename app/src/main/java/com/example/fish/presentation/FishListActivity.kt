package com.example.fish.presentation

import android.os.Bundle
import com.example.fish.R
import com.example.fish.common.ErrorCode
import com.example.fish.common.base.BaseActivity
import com.example.fish.common.extension.gone
import com.example.fish.common.extension.visible
import com.example.fish.common.viewbinding.viewBinding
import com.example.fish.common.viewstate.ListViewState
import com.example.fish.databinding.ActivityFishListBinding
import com.example.fish.domain.model.FishData
import org.koin.androidx.viewmodel.ext.android.viewModel

class FishListActivity : BaseActivity() {

    private val binding by viewBinding(ActivityFishListBinding::inflate)

    private val viewModel by viewModel<FishListViewModel>()

    private val adapter by lazy { FishAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        initListener()
        startObservingData()
        viewModel.getFishs()
    }

    private fun initView() {
        binding.rvFishs.adapter = adapter
    }

    private fun initListener() {
        binding.srlFishs.apply {
            setOnRefreshListener {
                isRefreshing = false
                adapter.clear()
                viewModel.getFishs()
            }
        }
    }

    private fun startObservingData() {
        viewModel.fishsState.observe(this) { state ->
            when (state) {
                is ListViewState.Loading -> {
                    hideError()
                    showLoading()
                }

                else -> {
                    hideLoading()
                    when (state) {
                        is ListViewState.Success -> bindView(state.data)
                        is ListViewState.Error -> showError(state.viewError?.errorCode.toString()) { viewModel.getFishs() }
                        is ListViewState.EmptyData -> showEmpty()
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.srlFishs.gone()
        binding.sflFishs.apply {
            visible()
            startShimmer()
        }
    }

    private fun hideLoading() {
        binding.sflFishs.apply {
            gone()
            stopShimmer()
        }
        binding.srlFishs.visible()
    }

    private fun showError(errorCode: String, action: () -> Unit) {
        binding.srlFishs.gone()
        when (errorCode) {
            ErrorCode.GLOBAL_INTERNET_ERROR -> {
                binding.viewError.tvTitle.text = getString(R.string.connection_error)
            }

            else -> {
                binding.viewError.tvTitle.text = getString(R.string.internal_server_error)
            }
        }
        binding.viewError.btnRetry.visible()
        binding.viewError.btnRetry.setOnClickListener { action.invoke() }
        binding.viewError.viewErrorContainer.visible()
    }

    private fun showEmpty() {
        binding.srlFishs.gone()
        binding.viewError.tvTitle.text = getString(R.string.empty_data)
        binding.viewError.btnRetry.gone()
        binding.viewError.viewErrorContainer.visible()
    }

    private fun hideError() {
        binding.srlFishs.visible()
        binding.viewError.viewErrorContainer.gone()
    }

    private fun bindView(data: List<FishData>) {
        adapter.resetData(data)
    }
}