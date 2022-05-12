package com.example.fish.di

import com.example.fish.data.repository.FishRepository
import com.example.fish.domain.source.FishDataSource
import org.koin.dsl.module

val repositoryModule = module {

    factory { FishRepository(get()) as FishDataSource }
}
