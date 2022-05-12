package com.example.fish.data.repository

import com.example.fish.data.remote.FishApiService
import com.example.fish.data.remote.response.FistListResponse
import com.example.fish.data.remote.response.OptionAreaResponse
import com.example.fish.data.remote.response.OptionSizeResponse
import com.example.fish.domain.source.FishDataSource

class FishRepository(private val fishApiService: FishApiService): FishDataSource {

    override suspend fun getOptionSize(): OptionSizeResponse = fishApiService.getOptionSize()

    override suspend fun getOptionArea(): OptionAreaResponse = fishApiService.getOptionArea()

    override suspend fun getFishList(): FistListResponse = fishApiService.getFishList()
}