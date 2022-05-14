package com.example.fish.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ApplyFilterData(
    val cities: List<String>,
    val provinces: List<String>,
    val sizes: List<String>
): Parcelable