package com.greencoins.app.data

/**
 * Reward categories – FINAL, do not change.
 */
val REWARD_CATEGORIES = listOf(
    "Travel",
    "Eco Store",
    "Lifestyle",
    "Food & Beverage",
    "Utilities",
    "Vouchers",
)

/**
 * Simple Reward data model for Shop/Marketplace.
 */
data class ShopReward(
    val id: String,
    val title: String,
    val coinCost: Int,
    val category: String,
)

/**
 * Hardcoded rewards – no backend, no network.
 */
object ShopRepository {
    private val allRewards = listOf(
        // Travel
        ShopReward("t1", "₹50 Metro Recharge", 250, "Travel"),
        ShopReward("t2", "Bus Pass Discount", 400, "Travel"),
        // Eco Store
        ShopReward("e1", "Reusable Bottle", 300, "Eco Store"),
        ShopReward("e2", "Cloth Tote Bag", 200, "Eco Store"),
        ShopReward("e3", "Eco Starter Kit", 600, "Eco Store"),
        // Lifestyle
        ShopReward("l1", "Café Voucher", 350, "Lifestyle"),
        ShopReward("l2", "Thrift Store Discount", 280, "Lifestyle"),
        ShopReward("l3", "Yoga / Fitness Pass", 500, "Lifestyle"),
        // Food & Beverage
        ShopReward("f1", "Plant-Based Meal Discount", 180, "Food & Beverage"),
        ShopReward("f2", "Organic Grocery Coupon", 420, "Food & Beverage"),
        ShopReward("f3", "Discount on Bill", 150, "Food & Beverage"),
        // Utilities
        ShopReward("u1", "Mobile Recharge Coupon", 100, "Utilities"),
        ShopReward("u2", "Electricity Bill Discount", 450, "Utilities"),
        ShopReward("u3", "Water Bill Credit", 220, "Utilities"),
        // Vouchers
        ShopReward("v1", "₹100 Generic Voucher", 500, "Vouchers"),
        ShopReward("v2", "Brand-Neutral Coupon", 350, "Vouchers"),
        ShopReward("v3", "Partner Offer Voucher", 700, "Vouchers"),
    )

    fun getRewardsByCategory(category: String): List<ShopReward> =
        allRewards.filter { it.category == category }

    fun getCategories(): List<String> = REWARD_CATEGORIES
}
