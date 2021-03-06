package com.example.fish.data.remote

import com.example.fish.data.remote.response.FistListResponse
import com.example.fish.data.remote.response.OptionAreaResponse
import com.example.fish.data.remote.response.OptionSizeResponse
import retrofit2.http.GET

interface FishApiService {

    @GET("option_size")
    suspend fun getOptionSize(): List<OptionSizeResponse>

    @GET("option_area")
    suspend fun getOptionArea(): List<OptionAreaResponse>

    @GET("list")
    suspend fun getFishList(): List<FistListResponse>
}