package com.greencoins.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.greencoins.app.components.GlassCard
import com.greencoins.app.data.Mission
import com.greencoins.app.data.MissionsData
import com.greencoins.app.theme.AppColors
import com.greencoins.app.ui.toImageVector

sealed class PlusStep { object Selection : PlusStep(); object Brief : PlusStep(); object Upload : PlusStep(); object Success : PlusStep() }

@Composable
fun PlusFlow(
    step: PlusStep,
    missionId: String?,
    onSelectMission: (String) -> Unit,
    onNext: () -> Unit,
    onCancel: () -> Unit,
) {
    val selectedMission = MissionsData.list.find { it.id == missionId } ?: MissionsData.list.first()
    when (step) {
        is PlusStep.Selection -> PlusSelectionStep(onSelectMission = onSelectMission, onCancel = onCancel)
        is PlusStep.Brief -> PlusBriefStep(mission = selectedMission, onNext = onNext, onCancel = onCancel)
        is PlusStep.Upload -> PlusUploadStep(onNext = onNext, onCancel = onCancel)
        is PlusStep.Success -> PlusSuccessStep(mission = selectedMission, onCancel = onCancel)
    }
}

@Composable
private fun PlusSelectionStep(onSelectMission: (String) -> Unit, onCancel: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 48.dp, bottom = 96.dp, start = 24.dp, end = 24.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Select Mission", color = AppColors.white, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = onCancel) {
                Icon(Icons.Default.Add, contentDescription = null, tint = AppColors.textSecondary, modifier = Modifier.size(24.dp).graphicsLayer { rotationZ = 45f })
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        MissionsData.list.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                row.forEach { m ->
                    GlassCard(
                        modifier = Modifier.weight(1f),
                        onClick = { onSelectMission(m.id) },
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(AppColors.border)
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(m.iconId.toImageVector(), contentDescription = null, tint = AppColors.accent, modifier = Modifier.size(24.dp))
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(m.title, color = AppColors.white, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(m.desc, color = AppColors.textSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("+${m.coins} COINS", color = AppColors.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PlusBriefStep(mission: Mission, onNext: () -> Unit, onCancel: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(48.dp, 48.dp, 24.dp, 40.dp),
    ) {
        IconButton(onClick = onCancel) {
            Icon(Icons.Default.Add, contentDescription = null, tint = AppColors.textSecondary, modifier = Modifier.size(24.dp).graphicsLayer { rotationZ = 45f })
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text("MISSION BRIEFING", color = AppColors.accent, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text("Mission: ${mission.title}", color = AppColors.white, fontSize = 36.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(48.dp))
        listOf(
            "Identify a suitable native planting spot",
            "Dig a hole 2x the size of the root ball",
            "Plant sapling and water the soil",
        ).forEachIndexed { index, text ->
            Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 24.dp)) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(AppColors.accent.copy(alpha = 0.05f), CircleShape)
                        .border(1.dp, AppColors.accent, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("${index + 1}", color = AppColors.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.size(24.dp))
                Text(text, color = AppColors.white, fontSize = 18.sp, modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth().height(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.accent, contentColor = AppColors.black),
            shape = RoundedCornerShape(24.dp),
        ) {
            Text("Start Mission", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.size(12.dp))
            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun PlusUploadStep(onNext: () -> Unit, onCancel: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(48.dp, 48.dp, 24.dp, 40.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Proof of Impact", color = AppColors.white, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = onCancel) {
                Icon(Icons.Default.Add, contentDescription = null, tint = AppColors.textSecondary, modifier = Modifier.size(24.dp).graphicsLayer { rotationZ = 45f })
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .background(AppColors.border.copy(alpha = 0.3f), RoundedCornerShape(32.dp))
                    .border(2.dp, AppColors.border, RoundedCornerShape(32.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, tint = AppColors.textSecondary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Before", color = AppColors.textSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .background(AppColors.accent.copy(alpha = 0.05f), RoundedCornerShape(32.dp))
                    .border(2.dp, AppColors.accent.copy(alpha = 0.5f), RoundedCornerShape(32.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null, tint = AppColors.accent, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("After Image", color = AppColors.accent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppColors.border, RoundedCornerShape(24.dp))
                .border(1.dp, AppColors.gray333)
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Place, contentDescription = null, tint = AppColors.accent, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.size(12.dp))
                Text("GPS: 28.6139° N, 77.2090° E", color = AppColors.white, fontSize = 14.sp)
            }
            Text("LOCKED", color = AppColors.textSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
        TextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp),
            placeholder = { Text("Tell us about your impact (optional)", color = AppColors.gray555) },
            colors = TextFieldDefaults.colors(
                focusedTextColor = AppColors.white,
                unfocusedTextColor = AppColors.white,
                focusedContainerColor = AppColors.border,
                unfocusedContainerColor = AppColors.border,
                focusedIndicatorColor = AppColors.accent.copy(alpha = 0.5f),
                unfocusedIndicatorColor = AppColors.gray333,
            ),
            shape = RoundedCornerShape(24.dp),
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth().height(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.accent, contentColor = AppColors.black),
            shape = RoundedCornerShape(24.dp),
        ) {
            Text("Submit for Verification", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PlusSuccessStep(mission: Mission, onCancel: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(0.3f))
        Box(
            modifier = Modifier
                .size(128.dp)
                .background(AppColors.accent, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(64.dp), tint = AppColors.black)
        }
        Spacer(modifier = Modifier.height(40.dp))
        Text("Request Sent!", color = AppColors.white, fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Our AI is verifying your mission proof. You'll receive coins once approved (est. 15 mins).",
            color = AppColors.textSecondary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(48.dp))
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(AppColors.border)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(mission.iconId.toImageVector(), contentDescription = null, tint = AppColors.accent, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.size(12.dp))
                    Column {
                        Text(mission.title, color = AppColors.white, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Just now", color = AppColors.textSecondary, fontSize = 10.sp)
                    }
                }
                Box(
                    modifier = Modifier
                        .background(AppColors.pendingYellowBg, RoundedCornerShape(9999.dp))
                        .border(1.dp, AppColors.pendingYellowBorder, RoundedCornerShape(9999.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                ) {
                    Text("PENDING", color = AppColors.pendingYellow, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth().height(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.white, contentColor = AppColors.black),
            shape = RoundedCornerShape(24.dp),
        ) {
            Text("Back to Dashboard", fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }
}
