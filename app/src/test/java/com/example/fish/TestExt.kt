package com.example.fish

import androidx.lifecycle.LiveData

fun <T> LiveData<T>.getTestObserver() = TestObserver<T>().apply {
    observeForever(this)
}