package com.example.fish.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fish.common.viewstate.ListViewState
import com.example.fish.domain.model.FishData
import com.example.fish.domain.usecase.GetFishListUseCase
import com.example.fish.domain.usecase.GetOptionAreaUseCase
import com.example.fish.domain.usecase.GetOptionSizeUseCase
import kotlinx.coroutines.launch

class FishListViewModel(
    private val getOptionSizeUseCase: GetOptionSizeUseCase,
    private val getOptionAreaUseCase: GetOptionAreaUseCase,
    private val getFishListUseCase: GetFishListUseCase
) : ViewModel() {

    val fishsState = MutableLiveData<ListViewState<List<FishData>>>()

    fun getFishs() {
        viewModelScope.launch {
            fishsState.postValue(ListViewState.Loading())
            getFishListUseCase.invoke(Unit).handleResult({ fishData ->
                fishsState.postValue(ListViewState.Success(fishData))
            }, { viewError ->
                fishsState.postValue(ListViewState.Error(viewError))
            })
        }
    }
}