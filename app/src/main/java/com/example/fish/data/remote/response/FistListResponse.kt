package com.example.fish.data.remote.response

import com.google.gson.annotations.SerializedName

data class FistListResponse(
    val uuid: String?,
    val komoditas: String?,
    @SerializedName("area_provinsi") val provinceArea: String?,
    @SerializedName("area_kota") val cityArea: String?,
    val size: String?,
    val price: String?,
    @SerializedName("tgl_parsed") val parseDate: String?,
    val timestamp: String?
)