package com.example.fish.di

import com.example.fish.presentation.FishListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { FishListViewModel(get(), get(), get()) }
}