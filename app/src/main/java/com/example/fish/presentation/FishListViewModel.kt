package com.example.fish.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fish.common.viewstate.ViewState
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

    val fishsState = MutableLiveData<ViewState<List<FishData>>>()

    fun getFishs() {
        viewModelScope.launch {
            fishsState.postValue(ViewState.Loading())
            getFishListUseCase.invoke(Unit).handleResult({ fishData ->
                fishsState.postValue(ViewState.Success(fishData))
            }, { viewError ->
                fishsState.postValue(ViewState.Error(viewError))
            })
        }
    }
}