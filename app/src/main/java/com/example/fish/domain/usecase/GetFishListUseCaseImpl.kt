package com.example.fish.domain.usecase

import com.example.fish.common.extension.asErrorObject
import com.example.fish.common.viewstate.ViewError
import com.example.fish.data.remote.response.FistListResponse
import com.example.fish.domain.Either
import com.example.fish.domain.model.FishData
import com.example.fish.domain.source.FishDataSource

class GetFishListUseCaseImpl(private val repository: FishDataSource): GetFishListUseCase {

    override suspend fun invoke(params: Unit): Either<List<FishData>, ViewError> {
        return try {
            val result = repository.getFishList()
            Either.Success(result.map(fishMapper))
        } catch (e: Exception) {
            Either.Fail(e.asErrorObject())
        }
    }

    private val fishMapper: (FistListResponse) -> FishData = { fish ->
        FishData(fish.uuid, fish.commodity, fish.provinceArea, fish.cityArea, fish.size, fish.price, fish.timestamp)
    }
}