package com.greencoins.app.data

import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UserRepository {
    private val client = SupabaseManager.client

    // Fetch the detailed profile from 'public.users' table
    suspend fun getProfile(userId: String): UserProfile? = withContext(Dispatchers.IO) {
        try {
            client.from("users").select(columns = Columns.list("id", "email", "full_name", "avatar_url", "eco_score", "total_gc")) {
                filter {
                    eq("id", userId)
                }
            }.decodeSingleOrNull<UserProfile>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Update specific fields
    suspend fun updateEcoScore(userId: String, newScore: Int) = withContext(Dispatchers.IO) {
        try {
            client.from("users").update(
                {
                    UserProfile::ecoScore setTo newScore
                }
            ) {
                filter {
                    eq("id", userId)
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}
