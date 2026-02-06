package com.greencoins.app.screens

import androidx.lifecycle.ViewModel
import com.greencoins.app.data.ShopRepository

class ShopViewModel : ViewModel() {
    private val repository = ShopRepository

    val categories: List<String> = repository.getCategories()

    fun getRewardsForCategory(category: String) = repository.getRewardsByCategory(category)
}
