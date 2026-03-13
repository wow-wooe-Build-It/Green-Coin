package com.greencoins.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
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
import com.greencoins.app.screens.ChallengeDetailScreen
import com.greencoins.app.data.ChallengeDetailData
import com.greencoins.app.screens.ProfileScreen
import com.greencoins.app.screens.ShopScreen
import com.greencoins.app.screens.ShopViewModel
import com.greencoins.app.theme.AppColors
import androidx.compose.runtime.collectAsState
import com.greencoins.app.data.AuthRepository
import com.greencoins.app.data.UserCoinsRepository
import androidx.compose.material3.CircularProgressIndicator
import kotlinx.coroutines.launch

@Composable
fun GreenCoinsApp() {
    val isLoggedIn by AuthRepository.isLoggedIn.collectAsState()
    
    // We defer rendering until we know the login state (null means loading)
    if (isLoggedIn == null) {
         Box(modifier = Modifier.fillMaxSize().background(AppColors.bg), contentAlignment = Alignment.Center) {
             CircularProgressIndicator(color = AppColors.accent)
         }
         return
    }

    var screen by remember { 
        mutableStateOf<Screen>(
            if (isLoggedIn == true) Screen.Home else Screen.Auth
        ) 
    }

    // Force redirection when authentication state changes externally (like OAuth deeplink return)
    androidx.compose.runtime.LaunchedEffect(isLoggedIn) {
        if (isLoggedIn == true && screen == Screen.Auth) {
            screen = Screen.Home
        } else if (isLoggedIn == false && screen != Screen.Auth) {
            screen = Screen.Auth
        }
    }

    var coins by remember { mutableStateOf(0) }
    var plusStep by remember { mutableStateOf<PlusStep>(PlusStep.Selection) }
    var selectedMissionId by remember { mutableStateOf<String?>(null) }
    var selectedShopCategory by remember { mutableStateOf<String?>(null) }
    var selectedChallenge by remember { mutableStateOf<com.greencoins.app.data.ChallengeDetailData?>(null) }
    var joinedChallengeIds by remember { mutableStateOf(setOf<String>()) }
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

    // Load coins for the currently logged-in user once we know auth state
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn == true) {
            val userId = AuthRepository.currentUser?.id
            if (userId != null) {
                coins = UserCoinsRepository.getUserCoins(userId)
            }
        } else {
            coins = 0
        }
    }

    if (screen == Screen.Auth) {
        AuthScreen(onLogin = { screen = Screen.Home })
        return
    }

    BackHandler(enabled = screen != Screen.Home) {
        when {
            screen == Screen.Help -> screen = Screen.Home
            screen == Screen.Plus -> screen = Screen.Home
            screen == Screen.Shop && selectedShopCategory != null -> selectedShopCategory = null
            screen == Screen.Shop -> screen = Screen.Home
            screen == Screen.Challenges -> screen = Screen.Home
            screen == Screen.ChallengeDetail -> screen = Screen.Challenges
            screen == Screen.Profile -> screen = Screen.Home
            else -> { }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.bg),
    ) {
        if (screen != Screen.Plus && screen != Screen.Help) {
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                Header(coins = coins, onHelp = { screen = Screen.Help })
                val onBack = when {
                    screen == Screen.Home -> null
                    screen == Screen.Shop && selectedShopCategory != null -> { { selectedShopCategory = null } }
                    screen == Screen.Shop -> { { screen = Screen.Home } }
                    screen == Screen.Challenges -> { { screen = Screen.Home } }
                    screen == Screen.ChallengeDetail -> { { screen = Screen.Challenges } }
                    screen == Screen.Profile -> { { screen = Screen.Home } }
                    else -> null
                }
                if (onBack != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 8.dp),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AppColors.textSecondary)
                        }
                    }
                }
            }
        }
        val showBackRow = screen != Screen.Plus && screen != Screen.Help && screen != Screen.Home
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = when {
                        screen == Screen.Plus || screen == Screen.Help -> 0.dp
                        showBackRow -> 64.dp + 56.dp
                        else -> 64.dp
                    }
                ),
        ) {
            AnimatedContent(
                targetState = screen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen",
            ) { current ->
                when (current) {
                    Screen.Auth -> { }
                    Screen.Home -> HomeScreen(
                        onMissionSelect = ::handleMissionSelect,
                        onChallengeClick = { data ->
                            selectedChallenge = data
                            screen = Screen.ChallengeDetail
                        },
                    )
                    Screen.Shop -> when {
                        selectedShopCategory == null -> ShopScreen(
                            categories = shopViewModel.categories,
                            onCategoryClick = { selectedShopCategory = it },
                        )
                        else -> {
                            val refreshScope = rememberCoroutineScope()
                            CategoryRewardsScreen(
                                categories = shopViewModel.categories,
                                selectedCategory = selectedShopCategory!!,
                                onCategoryChange = { selectedShopCategory = it },
                                onRedeem = {
                                    val userId = AuthRepository.currentUser?.id
                                    if (userId != null) {
                                        refreshScope.launch {
                                            coins = UserCoinsRepository.getUserCoins(userId)
                                        }
                                    }
                                },
                                onBack = { selectedShopCategory = null },
                            )
                        }
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
                    Screen.Challenges -> ChallengesScreen(
                        onChallengeClick = { data ->
                            selectedChallenge = data
                            screen = Screen.ChallengeDetail
                        }
                    )
                    Screen.ChallengeDetail -> if (selectedChallenge != null) {
                        ChallengeDetailScreen(
                            data = selectedChallenge!!,
                            onBack = { screen = Screen.Challenges },
                            isJoined = selectedChallenge!!.id in joinedChallengeIds,
                            onJoin = { joinedChallengeIds = joinedChallengeIds + selectedChallenge!!.id },
                        )
                    } else {
                        screen = Screen.Challenges // Fallback
                    }
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
