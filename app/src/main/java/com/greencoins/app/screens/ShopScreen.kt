package com.greencoins.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.greencoins.app.components.ImageWithFallback
import com.greencoins.app.data.RewardsData
import com.greencoins.app.theme.AppColors
import com.greencoins.app.theme.GreenCoinsTheme

@Composable
fun ShopScreen() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp, bottom = 96.dp, start = 24.dp, end = 24.dp),
    ) {
        item(span = { GridItemSpan(2) }) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppColors.border, RoundedCornerShape(32.dp)),
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .size(48.dp)
                            .background(AppColors.accent.copy(alpha = 0.1f), RoundedCornerShape(24.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = AppColors.accent, modifier = Modifier.size(24.dp))
                    }
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Featured Reward", color = AppColors.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("Ultimate Eco-Kit", color = AppColors.white, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Redeem your coins for the premium sustainable lifestyle bundle.",
                            color = AppColors.textSecondary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(end = 80.dp),
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = AppColors.accent, contentColor = AppColors.black),
                            shape = RoundedCornerShape(16.dp),
                        ) {
                            Text("Redeem Now", fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    listOf("Metro", "Store", "Life").forEach { cat ->
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .fillMaxWidth()
                                    .background(AppColors.border, RoundedCornerShape(24.dp)),
                                contentAlignment = Alignment.Center,
                            ) {
                                Box(modifier = Modifier.size(24.dp).background(AppColors.accent.copy(alpha = 0.2f), RoundedCornerShape(8.dp)))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(cat, color = AppColors.textSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    listOf("Food", "Donate", "Vouchers").forEach { cat ->
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .fillMaxWidth()
                                    .background(AppColors.border, RoundedCornerShape(24.dp)),
                                contentAlignment = Alignment.Center,
                            ) {
                                Box(modifier = Modifier.size(24.dp).background(AppColors.accent.copy(alpha = 0.2f), RoundedCornerShape(8.dp)))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(cat, color = AppColors.textSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Text("Popular Rewards", color = AppColors.white, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        items(RewardsData.list) { r ->
            Column {
                Box(
                    modifier = Modifier
                        .aspectRatio(4f / 5f)
                        .background(AppColors.border, RoundedCornerShape(28.dp)),
                ) {
                    ImageWithFallback(
                        src = r.image,
                        contentDescription = r.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                    r.discount?.let { discount ->
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(12.dp)
                                .background(AppColors.accent, RoundedCornerShape(9999.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                        ) {
                            Text(discount, color = AppColors.black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(r.title, color = AppColors.white, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(12.dp).background(AppColors.accent, RoundedCornerShape(6.dp)))
                    Spacer(modifier = Modifier.size(6.dp))
                    Text("${r.price}", color = AppColors.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Shop Screen")
@Composable
private fun ShopScreenPreview() {
    GreenCoinsTheme {
        ShopScreen()
    }
}
