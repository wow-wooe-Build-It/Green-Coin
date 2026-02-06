package com.greencoins.app.data

import androidx.compose.ui.graphics.vector.ImageVector

// Screen type - preserved exactly
enum class Screen {
    Auth, Home, Shop, Plus, Challenges, Profile, MissionBrief, MissionUpload, MissionSuccess, ChallengeDetail, Help
}

// Mission icon type - we use a simple id; UI will map to Icon
data class Mission(
    val id: String,
    val title: String,
    val iconId: MissionIcon,
    val coins: Int,
    val desc: String
)

enum class MissionIcon { TreePine, Recycle, Leaf, Users, Trash2, Zap }

object MissionsData {
    val list = listOf(
        Mission("canopy", "Green Canopy", MissionIcon.TreePine, 250, "Plant a native tree"),
        Mission("cycle", "Cycle Loop", MissionIcon.Recycle, 150, "Verify recycling batch"),
        Mission("plastic", "Plastic-Free", MissionIcon.Leaf, 100, "Cleanup plastic waste"),
        Mission("pulse", "Community Pulse", MissionIcon.Users, 300, "NGO volunteer work"),
        Mission("clearance", "Eco-Clearance", MissionIcon.Trash2, 200, "Garbage cleanup"),
        Mission("custom", "Wildcard", MissionIcon.Zap, 50, "Propose eco-action"),
    )
}

data class Reward(
    val id: Int,
    val title: String,
    val price: Int,
    val category: String,
    val image: String,
    val discount: String?
)

object RewardsData {
    val list = listOf(
        Reward(1, "Metro Pass (1 Month)", 1200, "Metro Pass", "https://images.unsplash.com/photo-1712591009476-5fe03c2ea938?q=80&w=400", "20% OFF"),
        Reward(2, "Eco Bottle Pro", 800, "Eco Store", "https://images.unsplash.com/photo-1760863264228-fa0792a2d894?q=80&w=400", "FREE"),
        Reward(3, "Forest Donation", 500, "Direct Donate", "https://images.unsplash.com/photo-1647220576336-f2e94680f3b8?q=80&w=400", null),
        Reward(4, "Zero Waste Kit", 1500, "Lifestyle", "https://images.unsplash.com/photo-1759868412016-8b7da190992a?q=80&w=400", "15% OFF"),
    )
}

data class ActiveChallenge(
    val id: Int,
    val title: String,
    val progress: Int,
    val reward: Int,
    val timeLeft: String,
    val img: String
)

data class FeaturedChallenge(
    val id: Int,
    val title: String,
    val participants: String,
    val reward: String,
    val img: String
)

data class GlobalChallenge(
    val id: Int,
    val title: String,
    val region: String,
    val goal: String,
    val iconId: MissionIcon
)

object ChallengesData {
    val active = listOf(
        ActiveChallenge(1, "City Cleanup Drive", 65, 800, "2d 14h", "https://images.unsplash.com/photo-1757801720436-032c2e5b58c6?q=80&w=400"),
        ActiveChallenge(2, "Solar Transition", 30, 1500, "12d 5h", "https://images.unsplash.com/photo-1759266039803-1f81c04bd4c0?q=80&w=400"),
    )
    val featured = listOf(
        FeaturedChallenge(3, "Green Roof Initiative", "2.4k", "5,000 GC Pool", "https://images.unsplash.com/photo-1607194402064-d0742de6d17b?q=80&w=600"),
        FeaturedChallenge(4, "Coral Reef Revival", "850", "Exclusive Badge", "https://images.unsplash.com/photo-1741704445331-83ed820f0214?q=80&w=600"),
    )
    val global = listOf(
        GlobalChallenge(5, "Save the Amazon", "Brazil", "10k Trees", MissionIcon.TreePine),
        GlobalChallenge(6, "Ocean Plastic Barrier", "Pacific", "500kg Plastic", MissionIcon.Recycle),
        GlobalChallenge(7, "Eco-Smart Cities", "Global", "1M Users", MissionIcon.Zap),
    )
}
