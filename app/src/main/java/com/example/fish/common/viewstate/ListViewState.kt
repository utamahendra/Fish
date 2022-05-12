package com.example.fish.common.viewstate

sealed class ListViewState<T> {
    data class Loading<T>(var progress: Float? = null) : ListViewState<T>()
    data class Success<T>(var data: T) : ListViewState<T>()
    data class Error<T>(var viewError: ViewError? = null) : ListViewState<T>()
    data class EmptyData<T>(val unit: Unit = Unit) : ListViewState<T>()
}