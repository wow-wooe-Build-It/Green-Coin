package com.greencoins.app.data

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Screen type - preserved exactly
enum class Screen {
    Auth, Home, Shop, Plus, Challenges, Profile, MissionBrief, MissionUpload, MissionSuccess, ChallengeDetail, Help
}

enum class MissionIcon { TreePine, Recycle, Leaf, Users, Trash2, Zap }

@Serializable
data class UserProfile(
    val id: String,
    val email: String? = null,
    @SerialName("full_name") val fullName: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("eco_score") val ecoScore: Int = 0,
    @SerialName("total_gc") val totalGc: Int = 0,
)

@Serializable
data class Challenge(
    val id: String,
    val title: String,
    val description: String? = null,
    @SerialName("cover_image_url") val coverImageUrl: String? = null,
    @SerialName("reward_gc") val rewardGc: Int = 0,
    @SerialName("start_date") val startDate: String? = null,
    @SerialName("end_date") val endDate: String? = null,
    @SerialName("is_active") val isActive: Boolean = true,
)

@Serializable
data class Mission(
    val id: String,
    val title: String,
    val description: String? = null,
    @SerialName("icon_type") val iconType: String,
    @SerialName("gc_reward") val gcReward: Int = 0,
    @SerialName("challenge_id") val challengeId: String? = null
) {
    val icon: MissionIcon get() = try { MissionIcon.valueOf(iconType) } catch(e: Exception) { MissionIcon.Leaf }
}

@Serializable
data class Submission(
    val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("mission_id") val missionId: String,
    @SerialName("image_url") val imageUrl: String? = null,
    val status: String = "pending",
    @SerialName("created_at") val createdAt: String? = null
)

@Serializable
data class Reward(
    val id: String,
    val title: String,
    val description: String? = null,
    val category: String,
    @SerialName("gc_cost") val gcCost: Int,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("discount_label") val discountLabel: String? = null
)

@Serializable
data class Transaction(
    val id: String,
    @SerialName("user_id") val userId: String,
    val amount: Int,
    val description: String?,
    val type: String,
    @SerialName("created_at") val createdAt: String? = null
)


/** Unified challenge data for the detail screen. */
data class ChallengeDetailData(
    val id: String,
    val title: String,
    val img: String,
    val instructions: List<String>,
)

/** Leaderboard entry for challenge detail. */
data class LeaderboardEntry(
    val rank: Int,
    val username: String,
    val coins: Int,
    val isCurrentUser: Boolean,
)

object ChallengeDetailRepository {
    private const val PLACEHOLDER_IMG = "https://images.unsplash.com/photo-1647220576336-f2e94680f3b8?q=80&w=400"

    // Adapter for Supabase Challenge -> UI Detail Data
    fun toDetail(challenge: Challenge) = ChallengeDetailData(
        id = challenge.id,
        title = challenge.title,
        img = challenge.coverImageUrl ?: PLACEHOLDER_IMG,
        instructions = listOf(
            "Read the challenge description: ${challenge.description ?: "Make an impact!"}",
            "Perform the required eco-friendly actions.",
            "Upload your proof in the Missions tab to earn GC!"
        )
    )

    fun getMockLeaderboard(seed: Int): List<LeaderboardEntry> = listOf(
        LeaderboardEntry(1, "Kushagra Mehta", 450, true),
        LeaderboardEntry(2, "NeonRunner22", 410, false),
        LeaderboardEntry(3, "EcoGhost", 395, false),
        LeaderboardEntry(4, "GreenByte", 380, false),
        LeaderboardEntry(5, "SolarPunk", 360, false),
    )
}
