package com.example.fish.domain.usecase

import com.example.fish.common.extension.asErrorObject
import com.example.fish.common.viewstate.ViewError
import com.example.fish.data.remote.response.FistListResponse
import com.example.fish.domain.Either
import com.example.fish.domain.model.FishData
import com.example.fish.domain.source.FishDataSource

class GetFishListUseCaseImpl(private val repository: FishDataSource) : GetFishListUseCase {

    override suspend fun invoke(params: Unit): Either<List<FishData>, ViewError> {
        return try {
            val result = repository.getFishList()
            Either.Success(result.filter {
                !it.uuid.isNullOrBlank()
                        && !it.commodity.isNullOrBlank()
                        && (!it.provinceArea.isNullOrBlank() || !it.cityArea.isNullOrBlank())
                        && !it.size.isNullOrBlank()
                        && !it.price.isNullOrBlank()
            }.map(fishMapper))
        } catch (e: Exception) {
            Either.Fail(e.asErrorObject())
        }
    }

    private val fishMapper: (FistListResponse) -> FishData = { fish ->
        FishData(
            fish.uuid.toString(),
            fish.commodity.toString(),
            fish.provinceArea,
            fish.cityArea,
            fish.size.toString(),
            fish.price.toString()
        )
    }
}