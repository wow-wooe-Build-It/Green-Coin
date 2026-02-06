package com.greencoins.app.data

import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.Google
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

object AuthRepository {
    private val auth = SupabaseManager.client.auth

    suspend fun signUpWithEmail(email: String, pass: String, name: String, phone: String) {
        auth.signUpWith(Email) {
            this.email = email
            password = pass
            data = buildJsonObject {
                put("full_name", name)
                put("phone", phone)
            }
        }
    }

    suspend fun signInWithEmail(email: String, pass: String) {
        auth.signInWith(Email) {
            this.email = email
            password = pass
        }
    }

    suspend fun signInWithGoogle() {
        auth.signInWith(Google)
    }
    
    fun isUserLoggedIn(): Boolean {
        return auth.currentSessionOrNull() != null
    }
    
    suspend fun logout() {
        auth.signOut()
    }
}
