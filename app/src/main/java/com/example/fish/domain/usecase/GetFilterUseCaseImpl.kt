package com.example.fish.domain.usecase

import com.example.fish.common.extension.asErrorObject
import com.example.fish.common.viewstate.ViewError
import com.example.fish.data.remote.response.OptionAreaResponse
import com.example.fish.data.remote.response.OptionSizeResponse
import com.example.fish.domain.Either
import com.example.fish.domain.model.FilterData
import com.example.fish.domain.source.FishDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope

class GetFilterUseCaseImpl(private val repository: FishDataSource) : GetFilterUseCase {

    override suspend fun invoke(params: Unit): Either<FilterData, ViewError> {
        return try {
            supervisorScope {
                val areas = async { repository.getOptionArea() }
                val size = async { repository.getOptionSize() }
                Either.Success(filterMapper(areas.await(), size.await()))
            }
        } catch (e: Exception) {
            Either.Fail(e.asErrorObject())
        }
    }

    private fun filterMapper(
        areas: List<OptionAreaResponse>,
        sizes: List<OptionSizeResponse>
    ): FilterData {
        val cities = mutableListOf<String>()
        val provinces = mutableListOf<String>()
        val size = mutableListOf<String>()
        areas.map {
            cities.add(it.city)
            provinces.add(it.province)
        }
        sizes.map {
            size.add(it.size)
        }
        return FilterData(cities, provinces, size)
    }
}