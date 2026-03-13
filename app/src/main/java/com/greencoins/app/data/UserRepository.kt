package com.greencoins.app.data

import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UserRepository {
    private val client = SupabaseManager.client

    /** Fetch total_gc for header display (lifetime earned). */
    suspend fun getTotalGc(userId: String): Int = withContext(Dispatchers.IO) {
        try {
            val profile = client.from("users").select() {
                filter { eq("id", userId) }
            }.decodeSingleOrNull<UserProfile>()
            (profile?.totalGc ?: 0).coerceAtLeast(0)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    // Fetch the detailed profile from 'public.users' table
    suspend fun getProfile(userId: String): UserProfile? = withContext(Dispatchers.IO) {
        try {
            client.from("users").select() {
                filter { eq("id", userId) }
            }.decodeSingleOrNull<UserProfile>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /** Apply delta to total_gc (e.g. -cost on redeem). Returns new value or null on failure. */
    suspend fun updateTotalGcDelta(userId: String, delta: Int): Int? = withContext(Dispatchers.IO) {
        try {
            val current = getTotalGc(userId)
            val updated = (current + delta).coerceAtLeast(0)
            client.from("users").update(
                { UserProfile::totalGc setTo updated }
            ) { filter { eq("id", userId) } }
            updated
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
