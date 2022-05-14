package com.example.fish.presentation.filter

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.fish.R
import com.example.fish.common.extension.setWhiteNavigationBarBottomSheetDialog
import com.example.fish.common.viewbinding.viewBinding
import com.example.fish.databinding.ViewBottomSheetFilterBinding
import com.example.fish.domain.model.FilterData
import com.example.fish.domain.model.FilterListData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FishFilterBottomSheetFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(ViewBottomSheetFilterBinding::bind)
    private var behavior: BottomSheetBehavior<View>? = null
    private var filterData: FilterData? = null
    private var applyFilterListener: () -> Unit = {}
    private val cityFilterAdapter by lazy { CityFilterAdapter() }
    private val provinceFilterAdapter by lazy { ProvinceFilterAdapter() }
    private val sizeFilterAdapter by lazy { SizeFilterAdapter() }
    var city: MutableList<String> = arrayListOf()
    var province: MutableList<String> = arrayListOf()
    var size: MutableList<String> = arrayListOf()

    companion object {
        val TAG = FishFilterBottomSheetFragment::class.java.simpleName
        private const val ARG_BOTTOM_SHEET_FILTER = "filterData"
        fun newInstance(filterData: FilterData): FishFilterBottomSheetFragment {
            return FishFilterBottomSheetFragment().apply {
                arguments = bundleOf(ARG_BOTTOM_SHEET_FILTER to filterData)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.view_bottom_sheet_filter, container, false)
    }

    override fun getTheme(): Int = R.style.TransparentBottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { filterData = it.getParcelable(ARG_BOTTOM_SHEET_FILTER) }
        initRecyclerView()
        bindView()
        initListener()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener {
            dialog.let {
                val parentView = binding.root.parent as View
                behavior = BottomSheetBehavior.from(parentView)
                with((behavior as BottomSheetBehavior<View>)) {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    peekHeight = parentView.height
                }
                it.currentFocus
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dialog.setWhiteNavigationBarBottomSheetDialog()
        }

        return dialog
    }

    private fun initRecyclerView() {
        with(binding) {
            viewCityFilter.rvCity.adapter = cityFilterAdapter
            viewProvinceFilter.rvProvince.adapter = provinceFilterAdapter
            viewSizeFilter.rvSize.adapter = sizeFilterAdapter
        }
    }

    private fun bindView() {
        filterData?.apply {
            val result = mutableListOf<FilterListData>()
            cities.map {
                val filterListData = FilterListData(it, city.contains(it))
                result.add(filterListData)
            }
            cityFilterAdapter.clear()
            cityFilterAdapter.resetData(result)
            result.clear()

            provinces.map {
                val filterListData = FilterListData(it, province.contains(it))
                result.add(filterListData)
            }
            provinceFilterAdapter.resetData(result)
            result.clear()

            sizes.map {
                val filterListData = FilterListData(it, size.contains(it))
                result.add(filterListData)
            }
            sizeFilterAdapter.resetData(result)
            result.clear()
        }
    }

    private fun initListener() {
        cityFilterAdapter.setItemClickListener { _, item, _ ->
            item?.let {
                if (city.contains(it.name)) {
                    city.remove(it.name)
                } else {
                    city.add(it.name)
                }
            }
        }

        provinceFilterAdapter.setItemClickListener { _, item, _ ->
            item?.let {
                if (province.contains(it.name)) {
                    province.remove(it.name)
                } else {
                    province.add(it.name)
                }
            }
        }

        sizeFilterAdapter.setItemClickListener { _, item, _ ->
            item?.let {
                if (size.contains(it.name)) {
                    size.remove(it.name)
                } else {
                    size.add(it.name)
                }
            }
        }

        binding.ivClose.setOnClickListener { dismiss() }

        binding.btnFilter.setOnClickListener {
            applyFilterListener.invoke()
            dismiss()
        }

        behavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset <= 0f) dismiss()
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {}
        })
    }

    fun setListener(applyFilterListener: () -> Unit = { dismiss() }) {
        this.applyFilterListener = applyFilterListener
    }
}