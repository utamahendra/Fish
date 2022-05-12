package com.example.fish.presentation

import android.os.Bundle
import com.example.fish.common.base.BaseActivity
import com.example.fish.common.viewbinding.viewBinding
import com.example.fish.databinding.ActivityFishListBinding

class FishListActivity: BaseActivity() {

    private val binding by viewBinding(ActivityFishListBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}