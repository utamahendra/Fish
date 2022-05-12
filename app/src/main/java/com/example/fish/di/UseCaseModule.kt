package com.example.fish.di

import com.example.fish.domain.usecase.*
import org.koin.dsl.module

val useCaseModule = module {

    factory { GetFishListUseCaseImpl(get()) as GetFishListUseCase }

    factory { GetOptionAreaUseCaseImpl(get()) as GetOptionAreaUseCase }

    factory { GetOptionSizeUseCaseImpl(get()) as GetOptionSizeUseCase }
}