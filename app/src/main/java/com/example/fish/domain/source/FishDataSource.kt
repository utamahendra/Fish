package com.example.fish.domain.source

import com.example.fish.data.remote.response.FistListResponse
import com.example.fish.data.remote.response.OptionAreaResponse
import com.example.fish.data.remote.response.OptionSizeResponse

interface FishDataSource {

    suspend fun getOptionSize(): List<OptionSizeResponse>

    suspend fun getOptionArea(): List<OptionAreaResponse>

    suspend fun getFishList(): List<FistListResponse>
}