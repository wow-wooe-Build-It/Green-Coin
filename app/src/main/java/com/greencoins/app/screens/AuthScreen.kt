package com.greencoins.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.greencoins.app.theme.GreenCoinsTheme
import coil.request.ImageRequest
import com.greencoins.app.theme.AppColors

/**
 * Preserved exactly: background glow, logo, title, subtitle, Google + Email buttons, terms text.
 */
@Composable
fun AuthScreen(onLogin: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.bg),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp)
                .size(400.dp)
                .background(Color(0x1AA2FF00), RoundedCornerShape(200.dp)),
        ) {}
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(AppColors.accent, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = AppColors.black,
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "GreenCoins",
                color = AppColors.white,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Rewarding your environmental impact",
                color = AppColors.textSecondary,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = onLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.white, contentColor = AppColors.black),
                shape = RoundedCornerShape(20.dp),
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data("https://www.google.com/favicon.ico").build(),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    contentScale = ContentScale.Fit,
                )
                Spacer(modifier = Modifier.size(12.dp))
                Text("Continue with Google", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onLogin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.accent),
                border = androidx.compose.foundation.BorderStroke(1.dp, AppColors.accent),
                shape = RoundedCornerShape(20.dp),
            ) {
                Text("Login with Email", fontWeight = FontWeight.Bold)
            }
        }
        Text(
            text = "By continuing you agree to our Terms of Service",
            color = AppColors.gray555,
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp),
        )
    }
}

@Preview(showBackground = true, name = "Auth Screen")
@Composable
private fun AuthScreenPreview() {
    GreenCoinsTheme {
        AuthScreen(onLogin = {})
    }
}
