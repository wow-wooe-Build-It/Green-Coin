package com.greencoins.app.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.greencoins.app.components.GlassCard
import com.greencoins.app.components.ImageWithFallback
import com.greencoins.app.data.Challenge
import com.greencoins.app.data.ChallengeRepository
import com.greencoins.app.data.Mission
import com.greencoins.app.data.MissionRepository
import com.greencoins.app.data.UserProfile
import com.greencoins.app.data.UserRepository
import com.greencoins.app.theme.AppColors
import com.greencoins.app.theme.GreenCoinsTheme
import com.greencoins.app.ui.toImageVector

import io.github.jan.supabase.auth.auth
import com.greencoins.app.data.SupabaseManager

@Composable
fun HomeScreen(onMissionSelect: (String) -> Unit) {
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var missions by remember { mutableStateOf<List<Mission>>(emptyList()) }
    var challenges by remember { mutableStateOf<List<Challenge>>(emptyList()) }

    LaunchedEffect(Unit) {
        val userId = SupabaseManager.client.auth.currentUserOrNull()?.id
        if (userId != null) {
            userProfile = UserRepository.getProfile(userId)
        }
        missions = MissionRepository.getMissions()
        challenges = ChallengeRepository.getChallenges()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 80.dp, bottom = 96.dp, start = 24.dp, end = 24.dp),
    ){
        // Hero - Impact Dashboard
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(240.dp),
            ) {
                val progress = 0.75f
                val ringColor = AppColors.accent
                
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 16.dp.toPx()
                    val startAngle = 140f
                    val sweepAngle = 260f
                    
                    // Background Track
                    drawArc(
                        color = ringColor.copy(alpha = 0.2f),
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                    
                    // Glow Effect
                    drawArc(
                         color = ringColor.copy(alpha = 0.2f),
                         startAngle = startAngle,
                         sweepAngle = sweepAngle * progress,
                         useCenter = false,
                         style = Stroke(width = strokeWidth + 12.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Foreground Ring
                    drawArc(
                        color = ringColor,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle * progress,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = (userProfile?.ecoScore ?: 0).toString(),
                        color = AppColors.white,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "ECO-SCORE",
                        color = AppColors.textSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("12", color = AppColors.white, fontWeight = FontWeight.Bold)
                    Text("Trees", color = AppColors.textSecondary, fontSize = 10.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("5kg", color = AppColors.white, fontWeight = FontWeight.Bold)
                    Text("Recycled", color = AppColors.textSecondary, fontSize = 10.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("140kg", color = AppColors.white, fontWeight = FontWeight.Bold)
                    Text("CO₂ Saved", color = AppColors.textSecondary, fontSize = 10.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        // Daily Missions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = "Daily Missions",
                color = AppColors.white,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "NEW REFRESH IN 4H",
                color = AppColors.accent,
                fontSize = 12.sp,
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            missions.take(2).forEach { m ->
                GlassCard(
                    modifier = Modifier.weight(1f),
                    onClick = { onMissionSelect(m.id) },
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(AppColors.border)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = m.icon.toImageVector(),
                                contentDescription = null,
                                tint = AppColors.accent,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(m.title, color = AppColors.white, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(m.description ?: "", color = AppColors.textSecondary, fontSize = 10.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("+${m.gcReward}", color = AppColors.accent, fontSize = 10.sp)
                            Spacer(modifier = Modifier.size(4.dp))
                            Box(modifier = Modifier.size(4.dp).background(AppColors.accent, CircleShape))
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            missions.drop(2).take(2).forEach { m ->
                GlassCard(
                    modifier = Modifier.weight(1f),
                    onClick = { onMissionSelect(m.id) },
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(AppColors.border)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = m.icon.toImageVector(),
                                contentDescription = null,
                                tint = AppColors.accent,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(m.title, color = AppColors.white, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(m.description ?: "", color = AppColors.textSecondary, fontSize = 10.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("+${m.gcReward}", color = AppColors.accent, fontSize = 10.sp)
                            Spacer(modifier = Modifier.size(4.dp))
                            Box(modifier = Modifier.size(4.dp).background(AppColors.accent, CircleShape))
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Active Challenges",
            color = AppColors.white,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            challenges.forEach { c ->
                Box(
                    modifier = Modifier
                        .width(280.dp)
                        .height(160.dp)
                        .padding(4.dp),
                ) {
                    val isPreview = LocalInspectionMode.current

                    if (isPreview) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(AppColors.border),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(c.title, color = AppColors.white)
                        }
                    } else {
                        ImageWithFallback(
                            src = c.coverImageUrl ?: "",
                            contentDescription = c.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Transparent,
                                        Color.Black,
                                    )
                                )
                            ),
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .background(AppColors.accent, androidx.compose.foundation.shape.RoundedCornerShape(9999.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp),
                            ) {
                                Text(
                                    text = "WIN ${c.rewardGc} COINS",
                                    color = AppColors.black,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            Text(c.title, color = AppColors.white, fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = {},
                            modifier = Modifier.height(32.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.white),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AppColors.white.copy(alpha = 0.2f)),
                        ) {
                            Text("Join", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Home Screen")
@Composable
private fun HomeScreenPreview() {
    GreenCoinsTheme {
        HomeScreen(onMissionSelect = {})
    }
}
