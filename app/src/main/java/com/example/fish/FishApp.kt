package com.example.fish

import android.app.Application
import com.example.fish.di.networkModule
import com.example.fish.di.repositoryModule
import com.example.fish.di.useCaseModule
import com.example.fish.di.viewModelModule
import org.koin.core.context.startKoin

class FishApp: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            modules(
                arrayListOf(
                    viewModelModule, useCaseModule, repositoryModule, networkModule
                )
            )
        }
    }
}