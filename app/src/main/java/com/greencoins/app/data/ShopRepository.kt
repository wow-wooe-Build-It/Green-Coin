package com.greencoins.app.data

import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

object ShopRepository {
    private val client = SupabaseManager.client

    // Fetch all rewards
    suspend fun getRewards(): List<Reward> = withContext(Dispatchers.IO) {
        try {
            client.from("rewards").select().decodeList<Reward>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // Get rewards by category
    suspend fun getRewardsByCategory(category: String): List<Reward> = withContext(Dispatchers.IO) {
        try {
            client.from("rewards").select {
                filter {
                    eq("category", category)
                }
            }.decodeList<Reward>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    val categories = listOf(
        "Travel",
        "Eco Store",
        "Lifestyle",
        "Food & Beverage",
        "Utilities",
        "Vouchers"
    )

    // Redeem a reward
    // NOTE: In production, this should be a Postgres Function to ensure atomic transaction (check balance -> deduct -> insert tx)
    // For MVP, we still perform client-side updates but keep Supabase as the single source of truth.
    suspend fun redeemReward(userId: String, rewardId: String, cost: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            // 1. Insert Transaction (Spend)
            val transaction = buildJsonObject {
                put("user_id", userId)
                put("amount", -cost) // Negative for spend
                put("description", "Redeemed reward")
                put("type", "redeem")
                put("related_reward_id", rewardId)
            }
            
            client.from("transactions").insert(transaction)

            // 2. Decrement user coin balance server-side
            val newBalance = UserCoinsRepository.applyDelta(userId, -cost)

            newBalance != null
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
