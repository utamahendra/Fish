package com.example.fish.domain.usecase

import com.example.fish.common.extension.asErrorObject
import com.example.fish.common.viewstate.ViewError
import com.example.fish.data.remote.response.OptionAreaResponse
import com.example.fish.domain.Either
import com.example.fish.domain.source.FishDataSource

class GetOptionSizeUseCaseImpl(private val repository: FishDataSource) : GetOptionSizeUseCase {

    override suspend fun invoke(params: Unit): Either<OptionAreaResponse, ViewError> {
        return try {
            val result = repository.getOptionArea()
            Either.Success(result)
        } catch (e: Exception) {
            Either.Fail(e.asErrorObject())
        }
    }
}