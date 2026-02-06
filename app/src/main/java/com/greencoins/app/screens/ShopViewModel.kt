package com.greencoins.app.screens

import androidx.lifecycle.ViewModel
import com.greencoins.app.data.ShopRepository

class ShopViewModel : ViewModel() {
    private val repository = ShopRepository


    // Categories are static for now, so safe to access synchronously
    val categories: List<String> = repository.categories
}
