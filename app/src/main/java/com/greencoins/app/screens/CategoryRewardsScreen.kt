package com.greencoins.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.greencoins.app.data.ShopReward
import com.greencoins.app.theme.AppColors

@Composable
fun CategoryRewardsScreen(
    categories: List<String>,
    rewards: List<ShopReward>,
    selectedCategory: String,
    onCategoryChange: (String) -> Unit,
    onRedeem: (ShopReward) -> Unit,
    onBack: () -> Unit,
) {
    var redeemedIds by remember { mutableStateOf(setOf<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.bg),
    ) {
        // Top bar with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 8.dp, end = 24.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = AppColors.white,
                )
            }
            Text(
                "Shop",
                color = AppColors.white,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        // Horizontal category selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            categories.forEach { category ->
                val isSelected = category == selectedCategory
                Box(
                    modifier = Modifier
                        .background(
                            color = if (isSelected) AppColors.accent else AppColors.border,
                            shape = RoundedCornerShape(9999.dp),
                        )
                        .clickable { onCategoryChange(category) }
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                ) {
                    Text(
                        text = category,
                        color = if (isSelected) AppColors.black else AppColors.textSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

        // Vertical list of rewards
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, top = 16.dp, end = 24.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            rewards.forEach { reward ->
                RewardCard(
                    reward = reward,
                    isRedeemed = reward.id in redeemedIds,
                    onRedeem = {
                        redeemedIds = redeemedIds + reward.id
                        onRedeem(reward)
                    },
                )
            }
        }
    }
}

@Composable
private fun RewardCard(
    reward: ShopReward,
    isRedeemed: Boolean,
    onRedeem: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.border, RoundedCornerShape(24.dp))
            .padding(20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reward.title,
                    color = AppColors.white,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(AppColors.accent, RoundedCornerShape(4.dp)),
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = "${reward.coinCost} GC",
                        color = AppColors.accent,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            Box(
                modifier = Modifier
                    .background(
                        color = if (isRedeemed) AppColors.textSecondary else AppColors.accent,
                        shape = RoundedCornerShape(16.dp),
                    )
                    .clickable(enabled = !isRedeemed) { onRedeem() }
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = if (isRedeemed) "Claimed" else "Redeem",
                    color = if (isRedeemed) AppColors.white else AppColors.black,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
