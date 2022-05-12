package com.example.fish.domain.usecase

import com.example.fish.data.remote.response.OptionAreaResponse
import com.example.fish.domain.UseCase

interface GetOptionAreaUseCase: UseCase<Unit, OptionAreaResponse>