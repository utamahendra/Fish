package com.example.fish.presentation.fishlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fish.common.viewstate.ListViewState
import com.example.fish.domain.model.FilterData
import com.example.fish.domain.model.FishData
import com.example.fish.domain.usecase.GetFilterUseCase
import com.example.fish.domain.usecase.GetFishListUseCase
import kotlinx.coroutines.launch

class FishListViewModel(
    private val getFilterUseCase: GetFilterUseCase,
    private val getFishListUseCase: GetFishListUseCase
) : ViewModel() {

    val fishesState = MutableLiveData<ListViewState<List<FishData>>>()

    val filterState = MutableLiveData<ListViewState<FilterData>>()

    fun getFishes() {
        viewModelScope.launch {
            fishesState.postValue(ListViewState.Loading())
            getFishListUseCase.invoke(Unit).handleResult({ fishData ->
                if (fishData.isEmpty()) {
                    fishesState.postValue(ListViewState.EmptyData())
                } else {
                    fishesState.postValue(ListViewState.Success(fishData))
                }
            }, { viewError ->
                fishesState.postValue(ListViewState.Error(viewError))
            })
        }
    }

    fun getFilterData() {
        viewModelScope.launch {
            getFilterUseCase.invoke(Unit).handleResult({ filterData ->
                filterState.postValue(ListViewState.Success(filterData))
            }, { viewError ->
                filterState.postValue(ListViewState.Error(viewError))
            })
        }
    }
}