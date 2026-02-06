package com.greencoins.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.remember
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.greencoins.app.components.GlassCard
import com.greencoins.app.components.ImageWithFallback
import com.greencoins.app.theme.AppColors

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 80.dp, bottom = 96.dp, start = 24.dp, end = 24.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(128.dp)
                        .padding(4.dp)
                        .background(AppColors.accent, RoundedCornerShape(48.dp)),
                ) {
                    ImageWithFallback(
                        src = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?q=80&w=200",
                        contentDescription = "Avatar",
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                Box(
                    modifier = Modifier
                        .offset(y = (-8).dp)
                        .background(AppColors.accent, RoundedCornerShape(9999.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                ) {
                    Text("LVL 24", color = AppColors.black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text("Kushagra Mehta", color = AppColors.white, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("#GC-492-9102-X", color = AppColors.textSecondary, fontSize = 12.sp)
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            GlassCard(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Total Earned", color = AppColors.textSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("12,450", color = AppColors.white, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.size(4.dp))
                        Text("GC", color = AppColors.accent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            GlassCard(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Global Rank", color = AppColors.textSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("#128", color = AppColors.white, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.size(4.dp))
                        Text("TOP 1%", color = AppColors.accent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        listOf(
            Icons.Default.Person to "Personal Information",
            Icons.Default.Star to "Impact Statistics",
            Icons.Default.ShoppingBag to "Redemption History",
            com.greencoins.app.ui.NavIcons.Help to "Help & Support",
        ).forEach { (icon, label) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(AppColors.border, RoundedCornerShape(24.dp))
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, contentDescription = null, tint = AppColors.textSecondary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(label, color = AppColors.white, fontWeight = FontWeight.Medium)
                }
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = AppColors.gray555, modifier = Modifier.size(18.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onLogout,
                )
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                "Logout Session",
                color = AppColors.redLogout,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
