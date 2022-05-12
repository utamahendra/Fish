package com.example.fish.domain.usecase

import com.example.fish.domain.UseCase
import com.example.fish.domain.model.FishData

interface GetFishListUseCase: UseCase<Unit, List<FishData>>