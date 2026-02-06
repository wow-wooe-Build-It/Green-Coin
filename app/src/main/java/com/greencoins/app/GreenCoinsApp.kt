package com.greencoins.app

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.greencoins.app.components.BottomNav
import com.greencoins.app.components.Header
import com.greencoins.app.data.Screen
import com.greencoins.app.screens.AuthScreen
import com.greencoins.app.screens.CategoryRewardsScreen
import com.greencoins.app.screens.ChallengesScreen
import com.greencoins.app.screens.HelpScreen
import com.greencoins.app.screens.HomeScreen
import com.greencoins.app.screens.PlusFlow
import com.greencoins.app.screens.PlusStep
import com.greencoins.app.screens.ProfileScreen
import com.greencoins.app.screens.ShopScreen
import com.greencoins.app.screens.ShopViewModel
import com.greencoins.app.theme.AppColors

@Composable
fun GreenCoinsApp() {
    var screen by remember { 
        mutableStateOf<Screen>(
            if (com.greencoins.app.data.AuthRepository.isUserLoggedIn()) Screen.Home else Screen.Auth
        ) 
    }
    var coins by remember { mutableStateOf(8420) }
    var plusStep by remember { mutableStateOf<PlusStep>(PlusStep.Selection) }
    var selectedMissionId by remember { mutableStateOf<String?>(null) }
    var selectedShopCategory by remember { mutableStateOf<String?>(null) }
    val shopViewModel: ShopViewModel = viewModel()

    fun handleScreenChange(s: Screen) {
        if (s == Screen.Plus) {
            plusStep = PlusStep.Selection
            screen = Screen.Plus
        } else {
            if (s == Screen.Shop) selectedShopCategory = null
            screen = s
        }
    }

    fun handleMissionSelect(id: String) {
        selectedMissionId = id
        plusStep = PlusStep.Brief
        screen = Screen.Plus
    }

    if (screen == Screen.Auth) {
        AuthScreen(onLogin = { screen = Screen.Home })
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.bg),
    ) {
        if (screen != Screen.Plus && screen != Screen.Help) {
            Box(modifier = Modifier.align(Alignment.TopCenter)) {
                Header(coins = coins, onHelp = { screen = Screen.Help })
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = if (screen != Screen.Plus && screen != Screen.Help) 64.dp else 0.dp),
        ) {
            AnimatedContent(
                targetState = screen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen",
            ) { current ->
                when (current) {
                    Screen.Auth -> { }
                    Screen.Home -> HomeScreen(onMissionSelect = ::handleMissionSelect)
                    Screen.Shop -> when {
                        selectedShopCategory == null -> ShopScreen(
                            categories = shopViewModel.categories,
                            onCategoryClick = { selectedShopCategory = it },
                        )
                        else -> CategoryRewardsScreen(
                            categories = shopViewModel.categories,
                            selectedCategory = selectedShopCategory!!,
                            onCategoryChange = { selectedShopCategory = it },
                            onRedeem = { /* UI feedback only – "Claimed" state shown in card */ },
                            onBack = { selectedShopCategory = null },
                        )
                    }
                    Screen.Help -> HelpScreen(onClose = { screen = Screen.Home })
                    Screen.Plus -> PlusFlow(
                        step = plusStep,
                        missionId = selectedMissionId,
                        onSelectMission = ::handleMissionSelect,
                        onNext = {
                            plusStep = when (plusStep) {
                                is PlusStep.Brief -> PlusStep.Upload
                                is PlusStep.Upload -> PlusStep.Success
                                else -> plusStep
                            }
                        },
                        onCancel = { screen = Screen.Home },
                    )
                    Screen.Challenges -> ChallengesScreen()
                    Screen.Profile -> ProfileScreen(onLogout = { screen = Screen.Auth })
                    else -> { }
                }
            }
        }
        if (screen != Screen.Plus) {
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                BottomNav(active = screen, onChange = ::handleScreenChange)
            }
        }
    }
}
